package com.nbacm.trelldochi.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.service.BoardService;
import com.nbacm.trelldochi.domain.common.config.JwtAuthenticationFilter;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    private ObjectMapper objectMapper;

    @Mock
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        when(customUserDetails.getUsername()).thenReturn("test@example.com");
    }

    // 보드 생성 성공
    @Test
    @WithMockUser(username = "test", roles = {"UserRole.ADMIN"})
    void createBoard_Success() throws Exception {
        Long workSpaceId = 1L;
        BoardRequestDto boardRequestDto = new BoardRequestDto("board", "white","", null);
        BoardResponseDto boardResponseDto = new BoardResponseDto();

        MockMultipartFile dtoFile = new MockMultipartFile(
                "dto", "", "application/json", objectMapper.writeValueAsBytes(boardRequestDto));
        MockMultipartFile imageFile = new MockMultipartFile(
                "image", "image.jpg", "image/jpeg", "test image content".getBytes()
        );

        when(boardService.createBoard(any(Long.class), any(BoardRequestDto.class), any(MockMultipartFile.class), eq(customUserDetails)))
                .thenReturn(boardResponseDto);

        mockMvc.perform(multipart("/api/v1/workspace/{workspaceId}", workSpaceId)
                .file(dtoFile)
                .file(imageFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("보드 생성 성공"));
    }
}
