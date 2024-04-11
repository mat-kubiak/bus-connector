//package com.github.mat_kubiak.tqs.bus_connector;
//
//import com.github.mat_kubiak.tqs.bus_connector.boundary.HomePageController;
//import com.github.mat_kubiak.tqs.bus_connector.service.ExchangeRateService;
//import com.github.mat_kubiak.tqs.bus_connector.service.ManagerServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.sql.Date;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(HomePageController.class)
//@AutoConfigureMockMvc
//class HomePageControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ExchangeRateService exchangeRateService; // Mock the service
//
//    @MockBean
//    private ManagerServiceImpl managerService; // Mock the service
//
//    @Test
//    void testHomePage() throws Exception {
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("index"))
//                .andExpect(model().attributeExists("cities"));
//    }
//
//    @Test
//    void testAboutPage() throws Exception {
//        Date date = TestUtil.getDate(TestUtil.getCurrentYear()+1, 5, 5);
//        String url = String.format("/search?from=1&to=2&date=%tF", date);
//        mockMvc.perform(get(url))
//                .andExpect(status().isOk())
//                .andExpect(view().name("about"))
//                .andExpect(model().attributeExists(HomePageController.originParam))
//                .andExpect(model().attributeExists(HomePageController.destParam))
//                .andExpect(model().attributeExists(HomePageController.dateParam))
//                .andExpect(model().attributeExists(HomePageController.dateIsoParam))
//                .andExpect(model().attributeExists("trips"))
//                .andExpect(model().attributeExists("isInPast"));
//    }
//
//}
