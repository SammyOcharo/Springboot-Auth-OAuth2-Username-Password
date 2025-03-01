package com.samdev.customizedOauth20.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samdev.customizedOauth20.DTO.UserDTO;
import com.samdev.customizedOauth20.Entity.User;
import com.samdev.customizedOauth20.ServiceImpl.JWTService;
import com.samdev.customizedOauth20.ServiceImpl.UserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@SuppressWarnings("unused")

public class SecurityFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailService userDetailService;
    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);


    public SecurityFilter(JWTService jwtService, UserDetailService userDetailService) {
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);

        }else {
            try {

                logger.info("We are on the filter, {}",  authHeader);

                String token = authHeader.substring(7);
                String username = jwtService.extractUsername(token);

                logger.info("We are on the username, {}",  username);

                System.out.println(SecurityContextHolder.getContext().getAuthentication());

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userDetailService.loadUserByUsername(username);
                    if (jwtService.isValid(token, user)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities()
                        );

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }catch (ExpiredJwtException ex) {
                UserDTO userDAO = new UserDTO();
                userDAO.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                userDAO.setResponseMessage("JWT token expired");

                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(userDAO);

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(json);
                return;
            }
            filterChain.doFilter(request, response);
        }

    }
}
