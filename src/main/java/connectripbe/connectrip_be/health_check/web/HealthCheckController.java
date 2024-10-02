package connectripbe.connectrip_be.health_check.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health-check")
@Tag(name = "Health Check", description = "애플리케이션 상태 확인")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<Void> HealthCheck() {
        return ResponseEntity.ok().build();
    }
}
