package kz.innlab.authservice.system.service.security.jwt

import java.io.IOException

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

import org.springframework.security.core.userdetails.UserDetailsService

class JwtAuthTokenFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val auth = SecurityContextHolder.getContext().authentication
            if (auth == null || !auth.isAuthenticated || auth.principal !is UserDetails) {
                filterChain.doFilter(request, response)
                return
            }

            try {
                val userDetails = userDetailsService.loadUserByUsername(auth.name)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authentication

                filterChain.doFilter(request, response)
            } catch (e: Exception) {
                logger.warn(e.message)
            }

        } catch (e: Exception) {
            logger.error("Can NOT set user authentication -> Message: {}", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun jwt(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")

        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.replace("Bearer ", "")
        } else null
    }
}
