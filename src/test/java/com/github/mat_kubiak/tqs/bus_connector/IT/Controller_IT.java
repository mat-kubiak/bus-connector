//package com.github.mat_kubiak.tqs.bus_connector.IT;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class Controller_IT {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void testGetAllCities() throws Exception {
//        mockMvc.perform(get("/api/cities"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].name").exists());
//    }
//
//    @Test
//    public void testReserveTicket() throws Exception {
//        // Assuming tripId, date, firstName, and lastName are valid parameters
//        Long tripId = 1L;
//        String date = "2024-04-10";
//        String firstName = "John";
//        String lastName = "Doe";
//
//        MvcResult result = mockMvc.perform(post("/api/ticket")
//                        .param("tripId", tripId.toString())
//                        .param("date", date)
//                        .param("firstName", firstName)
//                        .param("lastName", lastName))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Verify the response content if needed
//        String content = result.getResponse().getContentAsString();
//        // Add your verification logic here
//    }
//}
