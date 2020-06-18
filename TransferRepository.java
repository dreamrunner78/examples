package com.bas.backend.repository;

import com.bas.backend.beans.*;

import java.util.List;

public interface TransferRepository {

    //Transacton
    int count(String criteria);
    Transactions getValidatedTransactions(Integer offset, Integer limit, SearchCriteria searchCriteria);
    Transactions getBadTransactions(Integer offset, Integer limit, SearchCriteria searchCriteria);
    TransactionBackend getTransactionByID(String transferid);
    int saveSearchTransaction(SearchCriteria searchCriteria);
    List<SearchCriteria> getAllSearchProfiles();
    SearchCriteria getSearchProfileById(Long id);
    Transactions getAllTransactionsOfSearchProfiles(Long id, Integer offset, Integer limit);


    //Rule
    int addNewRule(Rule rule);
    int modifyRule(Rule rule);
    int enableDisableRule(Rule rule);
    Rules getAllRules();

    //Feature
    Features getAllFeatures();
    FeatureBackend getFeatureById(Integer featureid);
    int addNewFeature(FeatureBackend featureBackend);
    int modifyFeature(FeatureBackend featureBackend);
    Integer enableAllFeatures();

    //Scoring
    Scorings getAllScoring();
    ScoringBackend getScoringById(Integer scoringid);
    int addNewScoring(ScoringBackend scoringBackend);
    int modifyScoring(ScoringBackend scoringBackend);
    int enableDisableScoring(ScoringBackend scoringBackend);
    Integer enableAllScorings();


    //Dashboard
    List<TransactionResponseTime> getTransactionResponseTime();



}
