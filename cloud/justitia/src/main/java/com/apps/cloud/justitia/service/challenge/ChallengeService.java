package com.apps.cloud.justitia.service.challenge;

import java.util.List;
import java.util.Map;

public interface ChallengeService {

    List<Challenge> evaluate(String clientId, String username, Map<String, String> params);

}
