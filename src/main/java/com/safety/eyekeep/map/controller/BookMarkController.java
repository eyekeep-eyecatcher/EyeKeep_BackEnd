package com.safety.eyekeep.map.controller;

import com.safety.eyekeep.map.domain.BookMark;
import com.safety.eyekeep.map.dto.BookMarkDTO;
import com.safety.eyekeep.map.service.RepositoryService;
import com.safety.eyekeep.user.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookMarkController {
    private final RepositoryService repositoryService;

    @PostMapping("/save/bookmark")
    public ResponseEntity<?> saveBookMark(@RequestBody BookMark bookMark, Authentication authentication) {
        BookMarkDTO bookMarkDTO = createBookMarkDTO(bookMark, authentication);

        if (repositoryService.existsBookMarkByUsernameAndLocationName(bookMarkDTO)) {
            Map<String, Object> message = new HashMap<>();
            message.put("SaveBookMarkError", "Already exists book mark.");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        // 응답으로 저장된 북마크를 리턴.
        BookMark savedBookMark = repositoryService.saveBookMarkRepository(bookMarkDTO);
        if (savedBookMark == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("SaveBookMarkError", "Failed to save book mark.");
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(savedBookMark, HttpStatus.OK);
    }

    @PostMapping("/set/alias")
    public ResponseEntity<?> setAlias(@RequestBody BookMark bookMark, Authentication authentication) {
        BookMarkDTO bookMarkDTO = createBookMarkDTO(bookMark, authentication);

        BookMark completedBookMark = repositoryService.setAliasBookMarkRepository(bookMarkDTO);
        if (completedBookMark == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("SetAliasBookMarkError", "Failed to set alias at book mark.");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(completedBookMark, HttpStatus.OK);
    }

    @PostMapping("/delete/bookmark")
    public ResponseEntity<?> deleteBookMark(@RequestBody BookMark bookMark, Authentication authentication) {
        BookMarkDTO bookMarkDTO = createBookMarkDTO(bookMark, authentication);

        HttpStatus responseCode = repositoryService.deleteBookMarkByUsernameAndLocationName(bookMarkDTO);
        return new ResponseEntity<Void>(responseCode);
    }

    @GetMapping("/request/bookmark")
    public ResponseEntity<?> requestBookMark(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        BookMarkDTO bookMarkDTO = repositoryService.findBookMarkEntityByUsername(username);
        if (bookMarkDTO == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("RequestBookMarkError", "Failed to find book mark.");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        List<BookMark> bookMarkList = bookMarkDTO.getList();
        return new ResponseEntity<>(bookMarkList, HttpStatus.OK);
    }

    private static BookMarkDTO createBookMarkDTO(BookMark bookMark, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        BookMarkDTO bookMarkDTO = new BookMarkDTO();
        bookMarkDTO.setUsername(username);
        List<BookMark> bookMarkList = new ArrayList<>();
        bookMarkList.add(bookMark);
        bookMarkDTO.setList(bookMarkList);
        return bookMarkDTO;
    }
}
