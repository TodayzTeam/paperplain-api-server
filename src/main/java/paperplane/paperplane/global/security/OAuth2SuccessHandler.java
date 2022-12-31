package paperplane.paperplane.global.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.repository.UserRepository;
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.global.security.jwt.Token;
import paperplane.paperplane.global.security.jwt.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        Token token = tokenService.generateToken(email, "USER");

        Optional<User> userOptional = userRepository.findByEmail(email);

        //최초 로그인 시 회원가입
        if(userOptional.isEmpty()){
            userRepository.save(User.builder()
                    .email(email)
                    .name(oAuth2User.getAttribute("name"))
                    .refreshToken(token.getRefreshToken())
                    .profileImageUrl(oAuth2User.getAttribute("picture"))
                    .isPopularLetterEmail(false)
                    .isReadEmail(false)
                    .isRepliedEmail(false)
                    .isPopularLetterWeb(false)
                    .isReadWeb(false)
                    .isRepliedWeb(false)
                    .build());
        } else {
            User user = userOptional.get();
            user.setRefreshToken(token.getRefreshToken());
            userRepository.save(user);
        }

        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("accessToken", token.getAccessToken());
        response.addHeader("refreshToken", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000");
    }

}
