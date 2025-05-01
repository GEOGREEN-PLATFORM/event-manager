package com.example.event_manager.feignClient;

import com.example.event_manager.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name="user-servise", url="${user.service.host}")
public interface FeignClientUserService {
    @GetMapping("/user/by-id/{id}")
    UserDTO getUserById(@RequestHeader("Authorization") String token, @PathVariable UUID id);
}
