package e_commerce.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import e_commerce.agri.repository.CustomerRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final CustomerRepo customerRepository;

        public JwtAuthenticationFilter(
                        JwtService jwtService, CustomerRepo customerRepository) {

                this.jwtService = jwtService;
                this.customerRepository = customerRepository;
        }

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                        throws ServletException,
                        IOException {

                String authHeader = request.getHeader("Authorization");

                /*
                 * No Authorization header
                 */
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {

                        filterChain.doFilter(request, response);
                        return;
                }

                /*
                 * Extract token
                 */
                String token = authHeader.substring(7);

                try {

                        /*
                         * Extract email from JWT
                         */
                        String userEmail = jwtService.extractUsername(token);

                        if (userEmail == null) {

                                response.setStatus(
                                                HttpServletResponse.SC_UNAUTHORIZED);

                                response.getWriter().write("Invalid JWT token");

                                return;
                        }

                        /*
                         * Only authenticate if no authentication
                         * already exists
                         */
                        if (SecurityContextHolder.getContext().getAuthentication() == null) {

                                /*
                                 * Find user from DB
                                 */
                                UserDetails user = customerRepository.findByEmailId(userEmail)
                                                .map(x -> org.springframework.security.core.userdetails.User
                                                                .withUsername(x.getemailId())
                                                                .password(x.getPassword())
                                                                .build())
                                                .orElse(null);

                                /*
                                 * User doesn't exist
                                 */
                                if (user == null) {

                                        response.setStatus(
                                                        HttpServletResponse.SC_UNAUTHORIZED);

                                        response.getWriter().write("User not found");

                                        return;
                                }

                                /*
                                 * Validate JWT signature,
                                 * expiration, username, etc.
                                 */
                                if (!jwtService.isTokenValid(token, userEmail)) {

                                        response.setStatus(
                                                        HttpServletResponse.SC_UNAUTHORIZED);

                                        response.getWriter().write("Invalid or expired JWT token");

                                        return;
                                }

                                /*
                                 * Get existing HTTP session.
                                 *
                                 * IMPORTANT:
                                 * false means DON'T CREATE a new session.
                                 */
                                HttpSession session = request.getSession(false);

                                if (session == null) {

                                        response.setStatus(
                                                        HttpServletResponse.SC_UNAUTHORIZED);

                                        response.getWriter().write("Session expired");

                                        return;
                                }

                                /*
                                 * Get user stored during login
                                 */
                                String sessionUserEmail = (String) session.getAttribute("userEmail");

                                /*
                                 * Get current token stored in session
                                 */
                                String currentToken = (String) session.getAttribute("curToken");

                                /*
                                 * Make sure JWT belongs to the
                                 * same user as the session.
                                 */
                                if (sessionUserEmail == null || !sessionUserEmail.equals(userEmail)) {

                                        response.setStatus(
                                                        HttpServletResponse.SC_UNAUTHORIZED);

                                        response.getWriter().write("Invalid user session");

                                        return;
                                }

                                /*
                                 * VERY IMPORTANT
                                 *
                                 * Incoming token MUST be exactly
                                 * the current token stored in session.
                                 *
                                 * If TOKEN-1 is already replaced by
                                 * TOKEN-2, TOKEN-1 will fail here.
                                 */
                                if (!currentToken.equals(token)) {

                                        response.setStatus(
                                                        HttpServletResponse.SC_UNAUTHORIZED);

                                        response.getWriter().write("Old token is not valid");

                                        return;
                                }
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                                user, null, user.getAuthorities());

                                SecurityContextHolder.getContext().setAuthentication(authentication);

                                String newToken = jwtService.generateToken(userEmail);

                                session.setAttribute("curToken", newToken);

                                response.setHeader("Authorization", "Bearer " + newToken);
                        }

                } catch (Exception e) {

                        response.setStatus(
                                        HttpServletResponse.SC_UNAUTHORIZED);

                        response.getWriter().write("Invalid JWT token");

                        return;
                }

                /*
                 * Continue to controller
                 */
                filterChain.doFilter(
                                request, response);
        }
}