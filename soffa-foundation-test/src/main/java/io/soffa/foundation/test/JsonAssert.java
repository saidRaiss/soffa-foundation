package io.soffa.foundation.test;

import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class JsonAssert {

    private final ResultActions result;

    JsonAssert(ResultActions result) {
        this.result = result;
    }

    @SneakyThrows
    public JsonAssert eq(String path, Object value) {
        result.andExpect(jsonPath(path).value(Matchers.equalTo(value)));
        return this;
    }

    @SneakyThrows
    public JsonAssert isArray(String path) {
        result.andExpect(jsonPath(path).isArray());
        return this;
    }

}
