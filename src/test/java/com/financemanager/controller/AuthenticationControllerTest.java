package com.financemanager.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financemanager.dto.RoleDTO;
import com.financemanager.dto.UserDTO;
import com.financemanager.entity.payload.AuthRequest;
import com.financemanager.entity.payload.AuthResponse;
import com.financemanager.entity.payload.SaveUserRequest;
import com.financemanager.service.AuthService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = AuthenticationController.class,  useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AuthenticationController.class)})
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    
    @Autowired
    private MockMvc mvc;
    private ObjectMapper mapper;
    @MockBean
    private AuthService authService;
    
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }
    
    @Test
    void whenLogin_thenReturnStatus200() throws Exception {
        given(authService.login(Mockito.any(AuthRequest.class))).willReturn(new AuthResponse("token", "Bearer"));
        mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new AuthRequest("login", "password"))))
        .andExpect(status().isOk());
        verify(authService).login(Mockito.any(AuthRequest.class));
    }
    
    @Test
    void whenRegistration_thenReturnStatus201() throws Exception {
        given(authService.registration(Mockito.any(SaveUserRequest.class))).willReturn(prepareUserDTO());
        mvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new SaveUserRequest("name", "password", "email@gmail.com"))))
        .andExpect(status().isCreated());
        verify(authService).registration(Mockito.any(SaveUserRequest.class));
    }
    
    @Test
    void whenUpdate_thenReturnStatus200() throws Exception {
        UserDTO userDTO = prepareUserDTO();
        given(authService.updateName(Mockito.anyString(), Mockito.anyInt())).willReturn(userDTO);
        mvc.perform(put(String.format("/api/auth/update/%d", userDTO.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDTO.getName()))
        .andExpect(status().isOk());
        verify(authService).updateName(userDTO.getName(), userDTO.getId());
    }
    
    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(authService);
    }
    
    private UserDTO prepareUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("jurok3x@gmail.com");
        userDTO.setName("Yurii");
        userDTO.setRoleDTO(new RoleDTO(2, "ROLE_USER"));
        return userDTO;
    }

}
