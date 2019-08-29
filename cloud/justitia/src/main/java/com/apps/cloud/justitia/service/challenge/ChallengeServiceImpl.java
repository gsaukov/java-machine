package com.apps.cloud.justitia.service.challenge;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.apps.cloud.justitia.service.challenge.Challenge.Type.CREDENTIALS;
import static com.apps.cloud.justitia.service.challenge.Challenge.Type.ONE_TIME_PASSWORD;
import static org.apache.commons.lang.RandomStringUtils.random;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Override
    public List<Challenge> evaluate(String clientId, String username, Map<String, String> params) {
        List<Challenge> challenges = new ArrayList<>();

        challenges.add(new Challenge(CREDENTIALS));

        if (params.containsKey("transactionAmount")) {
            challenges.add(new Challenge(ONE_TIME_PASSWORD, randomOneTimePassword()));
        }

        return challenges;
    }

    private String randomOneTimePassword() {
        return random(6, true, true).toUpperCase();
    }

}
