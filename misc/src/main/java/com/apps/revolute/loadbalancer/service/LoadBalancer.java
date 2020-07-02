package com.apps.revolute.loadbalancer.service;

import com.apps.revolute.loadbalancer.entity.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadBalancer {

    public static final int MAX_PROVIDERS = 10;

    private final List<Provider> providers = new ArrayList<>();
    private final Random r;

    public LoadBalancer(int seed) {
        this.r = new Random(seed);
    }

    public void addProvider(Provider provider) throws IllegalStateException {
        if(providers.size() >= MAX_PROVIDERS){
            throw new IllegalStateException("The max number of accepted providers from the load balancer is 10");
        }

        if(providers.contains(provider)){
            throw new IllegalStateException("Provider is already registered");
        }

        providers.add(provider);
    }

    public Provider get() {
        return providers.get(r.nextInt(providers.size()));
    }

}


// So i did some retrospective analyses of LoadBalancer challenge based on Intellij feature Local history, it lets you see code changes in dynamic snapshots and here is what i can tell you.
//
// Where i fail

//1. i choosed wrong datastructure. interviewer could have helped with it, so i would not spent time to rewrite it later on.
//2. wrong exception. i offered enum as return type instead of exception, or just standard Exception.class to proceed further, it was declined by interviewer. I spent significant time finding one suitable in core java, i think i should have created my own, but it is inventing the bicycle. At least i m thankfull that we didnt had discussion about variable names.
//3. i didnt know URI.class; i would just use String, interviewer insisted on URI. So some extra time.
//4. i failed stepping out from array boundaries interviewer helped with it.
//5. i have URI in data structure should have put Provider.class. interviewer overlooked it but later on pointed that to me, so i had to rewrite it.
//6. tricky question with testing random. Interviewer gave two options, i chose one with seed but the time already passed.
//7. my screen was freezing so inteviewer asked me to restart could be my slow connection problems.

// what is checked:
// java core knowledge.
// ability to listen and synchronously code.
// ability to code fast.
// ability to code without mistakes
// ability to back your code with tests (biggest plus).

// what is not checked.
// problem solving. - There is no certain plan. You dont have full picture and you should reveal it. If you guess it right which is obviously not my case, you proceed further. My guess was wrong I wanted to implement LoadBalancer Based on consistent Hashing, i was playing with it not so long ago (https://github.com/gsaukov/java-machine/blob/master/depositary/src/main/java/com/apps/depositary/service/DepositExecutor.java)
// abstract or algorithmic thinking. - Most of the questions that were problematic are java specific(Choosing exception, URI, Seed).
// creativity or solution thinking. - You follow strict narrative from the interviewer.

// So yes interview proofs that: I do mistakes, I m not an expert in Java and I can't type fast. If that are the requirements to be hired then I think you are absolutely right. I m'not even looking for improvement in given areas.
// And i think the biggest thing that you miss is candidate's will, trait not to give up, keep pushing no matter what. I knew that i was failing, i saw my own mistakes, i saw that what i say contradicts to interviewer's opinion, but i tried till the end.
//
// I just had interview with another company, where you have to solve a problem: you are given a camera with field of view (angle int), and 2d array int[][] that has trees in it, Suppose you are in the center of this array, provide an angle that would fit maximum numbers of trees.
// Very good conversation, interviewer tries to look inside your brain and helps with ideas. Not just pattern matching.
// One hour for solution, no pressure, no restrictions, code how you think, just solve it.
