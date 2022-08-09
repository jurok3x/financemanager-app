package com.financemanager.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.financemanager.dto.RoleDTO;
import com.financemanager.dto.UserDTO;
import com.financemanager.service.UserService;
import com.financemanager.service.assembler.UserModelAssembler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

@WebMvcTest(value = UserController.class,  useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserController.class)})
@AutoConfigureMockMvc(addFilters = false)
@Import(UserModelAssembler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void whenFindById_thenReturnStatus200() throws Exception {
        UserDTO userDTO = prepareUserDTO();
        given(userService.findById(userDTO.getId())).willReturn(userDTO);
        mvc.perform(get(String.format("/api/users/%d", userDTO.getId())))
        .andExpect(status().isOk());
        verify(userService).findById(userDTO.getId());
    }
    
    @Test
    void whenFindByEmail_thenReturnStatus200() throws Exception {
        UserDTO userDTO = prepareUserDTO();
        given(userService.findByEmail(userDTO.getEmail())).willReturn(userDTO);
        mvc.perform(get(String.format("/api/users/email/%s", userDTO.getEmail())))
        .andExpect(status().isOk());
        verify(userService).findByEmail(userDTO.getEmail());
    }
    
    @Test
    void whenFindAll_thenReturnStatus200() throws Exception {
        UserDTO userDTO = prepareUserDTO();
        given(userService.findAll()).willReturn(Arrays.asList(userDTO));
        mvc.perform(get("/api/users"))
        .andExpect(status().isOk());
        verify(userService).findAll();
    }
    
    @Test
    void whenFindByCategoryId_thenReturnStatus200() throws Exception {
        UserDTO userDTO = prepareUserDTO();
        given(userService.findByCategoryId(Mockito.anyInt())).willReturn(Arrays.asList(userDTO));
        mvc.perform(get("/api/users/category/1"))
        .andExpect(status().isOk());
        verify(userService).findByCategoryId(Mockito.anyInt());
    }
    
    @Test
    void whenAddCategory_thenReturnStatus200() throws Exception {
        UserDTO userDTO = prepareUserDTO();
        given(userService.addCategory(Mockito.anyInt(), Mockito.anyInt())).willReturn(userDTO);
        mvc.perform(get(String.format("/api/users/%d/add/category/1", userDTO.getId())))
        .andExpect(status().isOk());
        verify(userService).addCategory(Mockito.anyInt(), Mockito.anyInt());
    }
    
    @Test
    void whenRemoveCategory_thenReturnStatus200() throws Exception {
        UserDTO userDTO = prepareUserDTO();
        given(userService.removeCategory(Mockito.anyInt(), Mockito.anyInt())).willReturn(userDTO);
        mvc.perform(get(String.format("/api/users/%d/remove/category/1", userDTO.getId())))
        .andExpect(status().isOk());
        verify(userService).removeCategory(Mockito.anyInt(), Mockito.anyInt());
    }
    
    @Test
    void whenDelete_thenReturnStatus204() throws Exception {
        UserDTO userDTO = prepareUserDTO();
        Mockito.doNothing().when(userService).delete(Mockito.anyInt());
        mvc.perform(delete(String.format("/api/users/%d", userDTO.getId())))
        .andExpect(status().isNoContent());
        verify(userService).delete(Mockito.anyInt());
    }
    
    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userService);
    }
    
    private UserDTO prepareUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("jurok3x@gmail.com");
        userDTO.setName("Yurii");
        userDTO.setPassword("metro090");
        userDTO.setRoleDTO(new RoleDTO(2, "ROLE_USER"));
        
        return userDTO;
    }

}
