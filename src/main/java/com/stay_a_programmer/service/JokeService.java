package com.stay_a_programmer.service;

import com.simtechdata.jokes.Jokes;
import com.simtechdata.jokes.classes.JokeError;
import com.simtechdata.jokes.enums.Category;
import com.simtechdata.jokes.enums.Language;
import com.simtechdata.jokes.enums.Type;
import com.stay_a_programmer.exception.JokeException;
import org.springframework.stereotype.Service;

@Service
public class JokeService {
    public String getJoke() {
        Jokes jokes = new Jokes.Builder()
                .addCategory(Category.PROGRAMMING)
                .setLanguage(Language.ENGLISH)
                .type(Type.SINGLE)
                .build();

        String joke = jokes.getAny();

        if (joke.contains("error")) {
            JokeError error = jokes.getError();
            throw new JokeException(error != null ? error.getMessage() : joke);
        }

        return joke;
    }
}
