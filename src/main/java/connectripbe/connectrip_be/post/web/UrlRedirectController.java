package connectripbe.connectrip_be.post.web;

import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.exception.NotFoundAccompanyPostException;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class UrlRedirectController {

    private final AccompanyPostRepository accompanyPostRepository;

    @GetMapping("/{customUrl}")
    public RedirectView redirectToOriginalUrl(@PathVariable String customUrl) {
        // customUrl을 사용하여 게시글을 조회
        AccompanyPostEntity post = accompanyPostRepository.findByCustomUrl(customUrl)
                .orElseThrow(NotFoundAccompanyPostException::new);

        // 게시글 ID를 사용하여 원래 경로로 리다이렉트
        return new RedirectView("https://connectrip.travel/accompany/" + post.getId());
    }
}
