package kg.attractor.labwork8.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public void configurationGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String userQuery = "select email, password, enabled\n" +
                "from users\n" +
                "where email = ?;";

        String roleQuery = "select u.email, a.authority\n" +
                "from users u\n" +
                "join roles r on u.role_id = r.id\n" +
                "join authorities a on r.authority_id = a.id\n" +
                "where u.email = ?;";

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(roleQuery);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(
                                        "/api/quizzes/*/solve",
                                        "/api/quizzes/*/rate",
                                        "/api/quizzes/*/results",
                                        "/api/users/*/statistics",
                                        "/api/quizzes"
                                ).fullyAuthenticated()

                                .requestMatchers(
                                        "/api/register",
                                        "/api/login",

                                        "/api/quizzes/*",
                                        "/api/quizzes/*/leaderboard"
                                ).permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                                .anyRequest().permitAll()
                );
        return http.build();
    }
}
