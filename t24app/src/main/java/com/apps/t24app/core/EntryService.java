package com.apps.t24app.core;

import com.apps.t24app.core.data.Entry;
import com.apps.t24app.persistance.repository.EntryRepository;

import org.apache.commons.math3.primes.Primes;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
//@Transactional transaction management from here.
public class EntryService {


    private EntryRepository entryRepository;

    private List<Entry> entries = new ArrayList<>();

    public String processEntry(Entry entry){
        entry.setStatus(processAge(entry.getAge()));
        entries.add(entry); //cool persistance;
        return entry.getStatus();
    }

    public String processAge(Integer age){
        if(Primes.isPrime(age)){
            return "FUNNY";
        }

        if (age == 10){
            return "STRIKE";
        }

        if(age < 18){
            return "TOO YOUNG";
        }

        if(age > 67) {
            return "TOO OLD";
        }

        return "OK";
    }

    public List<Entry> getEntries(){
        return entries;
    }

//    @Autowired
    public void setEntryRepository(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }
}
