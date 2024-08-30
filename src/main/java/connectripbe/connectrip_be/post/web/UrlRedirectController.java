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

    @GetMapping("/api/v1/accompany/{customUrl}")
    public RedirectView redirectToPostDetailPage(@PathVariable String customUrl) {
        // 단축 URL로 게시글을 조회
        AccompanyPostEntity post = accompanyPostRepository.findByCustomUrl(customUrl)
                .orElseThrow(NotFoundAccompanyPostException::new);

        return new RedirectView("/api/v1/accompany/posts/" + post.getId());
    }
}
