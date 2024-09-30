package connectripbe.connectrip_be.accompany.post.web;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.accompany.post.exception.NotFoundAccompanyPostException;
import connectripbe.connectrip_be.accompany.post.repository.AccompanyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class UrlRedirectController {

    private final AccompanyPostRepository accompanyPostRepository;

    @Value("${spring.customurl.success-redirect-url}")
    private String successRedirectUrl;

    @GetMapping("/{customUrl}")
    public RedirectView redirectToOriginalUrl(@PathVariable String customUrl) {
        AccompanyPostEntity post = accompanyPostRepository.findByCustomUrl(customUrl)
                .orElseThrow(NotFoundAccompanyPostException::new);

        return new RedirectView(successRedirectUrl + post.getId());
    }
}
