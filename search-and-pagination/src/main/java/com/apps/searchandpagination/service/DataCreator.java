package com.apps.searchandpagination.service;

public class DataCreator {

//    private RandomObjectFiller filler = new RandomObjectFiller();
//
//    @Autowired
//    private TradeDataRepository mkDataRepository;
//
//    @Autowired
//    private TradeDetailsRepository tradeDetailsRepository;
//
//    private void fillDataObjects() {
//        for(int i = 0; i < 1000; i++){
//            try {
//                TradeDetails tradeDetails = filler.createAndFill(TradeDetails.class);
//                tradeDetailsRepository.save(tradeDetails);
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public TradeDetails getTransaction(String detailId) {
//        TradeDetails transaction = null;
//        try {
//            transaction = filler.createAndFill(TradeDetails.class);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        transaction.setId(detailId);
//        transaction.setMix(getRandomWords(5));
//        transaction.setDetails(getRandomWords(10));
//        return transaction;
//    }
//
//    private String getRandomWords(int words){
//        String res = "";
//        for(int i = 0; i < words; i++){
//            res += RandomStringUtils.randomAlphabetic(1, 30);
//            res += " ";
//        }
//        return res;
//    }
//
//    private void fillDataObjects() {
//        for(int i = 0; i < 1000; i++){
//            try {
//                TradeData tradeData = filler.createAndFill(TradeData.class);
//                dataObjects.add(tradeData);
//                mkDataRepository.save(tradeData);
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
