package org.example.controller;

import org.example.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CourseControllerTest {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        // Wire controller with a real service instance (simple in-memory impl)
        CourseService service = new CourseService();
        CourseController controller = new CourseController();
        // If your controller has a field like @Autowired CourseService courseService,
        // add a constructor or setter; otherwise use reflection as a fallback:
        try {
            var f = CourseController.class.getDeclaredField("courseService");
            f.setAccessible(true);
            f.set(controller, service);
        } catch (NoSuchFieldException ignored) {
            // If your controller already has a constructor or setter, wire it there instead.
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mvc = standaloneSetup(controller).build();
    }

    @Test
    void createThenGetThenUpdateThenDelete() throws Exception {
        // Create
        mvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Spring Boot\", \"price\": 50.0}"))
                .andExpect(status().isCreated());

        // Get by id
        mvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Spring Boot")))
                .andExpect(jsonPath("$.price", is(50.0)));

        // Update
        mvc.perform(put("/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Spring Boot Pro\", \"price\": 75.0}"))
                .andExpect(status().isOk());

        // Verify update
        mvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Spring Boot Pro")))
                .andExpect(jsonPath("$.price", is(75.0)));

        // Delete
        mvc.perform(delete("/courses/1"))
                .andExpect(status().isNoContent());

        // Verify not found
        mvc.perform(get("/courses/1"))
                .andExpect(status().isNotFound());
    }
}
