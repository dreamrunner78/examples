package com.bas.tools;

public class Params {

    //Flink ParameterTools
    public static final String ptefile = "pte";

    //Job name
    public static final String engine = "engine";
    public static final String ruleJob = "rulejob";
    public static final String warmPercentile = "warmpercentile";
    public static final String warmDistinctDA = "warmdistinctda";
    public static final String warmDistinctCA = "warmdistinctca";

    //Constant kafka consumer producer
    public static final String consumer_features = "consumer_features";
    public static final String consumer_scorings = "consumer_scorings";
    public static final String consumer_rules = "consumer_rules";
    public static final String consumer_transfer_engine = "consumer_transfer_engine";
    public static final String consumer_transfer_warmPercentil = "consumer_transfer_warmPercentil";
    public static final String consumer_transfer_WarmDistinctDA = "consumer_transfer_WarmDistinctDA";
    public static final String consumer_transfer_WarmDistinctCA = "consumer_transfer_WarmDistinctCA";
    public static final String sink_error = "sink_error";
    public static final String sink_result = "sink_result";
    public static final String sink_blocked = "sink_blocked";
    public static final String sink_error_warmPercentil = "sink_error_warmPercentil";
    public static final String sink_error_warmDistinctDA = "sink_error_warmDistinctDA";
    public static final String sink_error_warmDistinctCA = "sink_error_warmDistinctCA";

    //UID
    public static final String uid_kafka_consumer_Features = "kafka_consumer_features";
    public static final String uid_kafka_consumer_Scorings = "uid_kafka_consumer_Scorings";
    public static final String uid_kafka_consumer_Rules = "kafka_consumer_rules";
    public static final String uid_kafka_consumer_transfer = "kafka_consumer_transfer";
    public static final String uid_map_transfer_warmpercentile = "uid_map_transfer_warmpercentile";
    public static final String uid_map_transfer_warmdistinctca = "uid_map_transfer_warmdistinctca";
    public static final String uid_map_transfer_warmdistinctda = "uid_map_transfer_warmdistinctda";
    public static final String uid_kafka_consumer_transfer_warmPercentil = "uid_kafka_consumer_transfer_warmPercentil";
    public static final String uid_kafka_consumer_transfer_warmdistinctda = "uid_kafka_consumer_transfer_warmdistinctda";
    public static final String uid_kafka_consumer_transfer_warmdistinctca = "uid_kafka_consumer_transfer_warmdistinctca";
    public static final String uid_filter_transfer = "filter_transfer";
    public static final String uid_filter_transfer_warmpercentil = "uid_filter_transfer_warmpercentil";
    public static final String uid_filter_transfer_warmdistinctda = "uid_filter_transfer_warmdistinctda";
    public static final String uid_filter_transfer_warmdistinctca = "uid_filter_transfer_warmdistinctca";
    public static final String uid_warmpercentilda = "uid_warmdistinctda";
    public static final String uid_warmpercentildaca = "uid_warmdistinctdaca";
    public static final String uid_warmpercentildacb = "uid_warmdistinctdacb";
    public static final String uid_warmpercentildacc = "uid_warmdistinctdacc";
    public static final String uid_warmdistinctcabyda = "uid_warmdistinctcabyda";
    public static final String uid_warmdistinctcabydacc = "uid_warmdistinctcabydacc";
    public static final String uid_warmdistinctdabyca = "uid_warmdistinctdabyca";
    public static final String uid_warmpercentilmapnotvalidated_transfer = "uid_warmpercentilmapnotvalidated_transfer";
    public static final String uid_warmpercentilsinknotvalidated_transfer = "uid_warmpercentilsinknotvalidated_transfer";
    public static final String uid_warmDistinctcamapnotvalidated_transfer = "uid_warmDistinctcamapnotvalidated_transfer";
    public static final String uid_warmDistinctcasinknotvalidated_transfer = "uid_warmDistinctcasinknotvalidated_transfer";
    public static final String uid_warmDistinctdamapnotvalidated_transfer = "uid_warmDistinctdamapnotvalidated_transfer";
    public static final String uid_warmDistinctdasinknotvalidated_transfer = "uid_warmDistinctdasinknotvalidated_transfer";




    public static final String uid_features_daca = "DebtorAccountAndCreditorAccount";
    public static final String uid_features_ca = "CreditorAccount";
    public static final String uid_features_da = "DebtorAccount";
    public static final String uid_features_dacb = "DebtorAccountAndCreditorBic";
    public static final String uid_features_dacc = "DebtorAccountAndCreditorCountry";
    public static final String uid_features_dacn = "DebtorAccountAndCreditorName";
    public static final String uid_join_stream = "join_stream";
    public static final String uid_apply_rule = "apply_rule";
    public static final String uid_filter_decision = "filter_decision";
    public static final String map_notvalidated_transfer = "map_notvalidated_stream";
    public static final String all_enriched_transfer = "all_enriched_transfer";
    public static final String blocked_enriched_transfer = "blocked_enriched_transfer";

    //States Descriptor
    public static final String featuresDescriptor = "featuresDescriptor";
    public static final String scoringsDescriptor = "scoringsDescriptor";
    public static final String rulesDescriptor = "rulesDescriptor";
    public static final String kieServicesDescriptor = "kieServicesDescriptor";
    public static final String enginegroovyDescriptor = "enginegroovyDescriptor";
    public static final String unionTransferDescriptor = "unionTransferDescriptor";
    public static final String unionTimerTransferDescriptor = "unionTimerTransferDescriptor";
    public static final String warmupPercentileDescriptor = "warmupPercentileDescriptor";
    public static final String warmupDistinctCADescriptor = "warmupDistinctCADescriptor";
    public static final String warmupDistinctDADescriptor = "warmupDistinctDADescriptor";

    //Broadcast
    public static final String map_features = "mapFeatures";
    public static final String map_rules = "mapRules";
    public static final String map_scorings = "mapScorings";

    //Side Output
    public static final String side_output_transfer = "side_output_transfer";
    public static final String side_output_transfer_warmpercentil = "side_output_transfer_warmpercentil";
    public static final String side_output_transfer_warmdistinctda = "side_output_transfer_warmdistinctda";
    public static final String side_output_transfer_warmdistinctca = "side_output_transfer_warmdistinctca";
    public static final String side_output_alert = "side_output_alert";
    public static final String side_output_timeoutasync = "side_output_timeoutasync";
    public static final String side_output_priority = "side_output_priority";

    //KeyLabels
    public static final String daca = "daca";
    public static final String ca = "ca";
    public static final String da = "da";
    public static final String dacb = "dacb";
    public static final String dacc = "dacc";
    public static final String dacn = "dacn";

    //TransferType
    public static final String ip = "ip";

    //Query Service
    public static final String queryService = "queryService";
    public static final String psqldb = "psqldb";
    public static final String psqldriver = "psqldriver";
    public static final String connectionredis = "connectionredis";
    public static final String connectionpsql = "connectionpsql";
    public static final String pong = "pong";

    //Features
    public static final String features = "features";
    public static final String strpercentiles = "strpercentiles";

    //Warm Engines
    public static final String percentileAmountByDebtorAccountANDCreditorAccount = "percentileAmountByDebtorAccountANDCreditorAccount";
    public static final String percentileAmountByDebtorAccount = "percentileAmountByDebtorAccount";
    public static final String percentileAmountByDebtorAccountANDCreditorBic = "percentileAmountByDebtorAccountANDCreditorBic";
    public static final String percentileAmountByDebtorAccountANDCreditorCountry = "percentileAmountByDebtorAccountANDCreditorCountry";

    public static final String nbDistinctDebtorAccountsByCreditorAccount = "nbDistinctDebtorAccountsByCreditorAccount";

    public static final String nbDistinctCreditorAccountsByDebtorAccount = "nbDistinctCreditorAccountsByDebtorAccount";
    public static final String nbDistinctCreditorAccountsByDebtorAccountandCreditorCountry = "nbDistinctCreditorAccountsByDebtorAccountandCreditorCountry";

    //Engines

    public static final String engineredis = "redis";
    public static final String enginepsql = "psql";
    public static final Integer timeout = 2000;
    public static final Integer capacity = 20;


    //Messages
    public static final String successenginefeatures = "Features computation succedded";
    public static final String timeoutenginefeatures = "Timeout Exception while computing Engine";

    //Steps
    public static final String eventkafka = "Collecting Data...   ";
    public static final String eventfiltering = "Cleaning Data...   ";
    public static final String ruleengine = "Applying Rules...   ";
    public static final String eventcomputesyncfeatures = "Compute Synchrounous Features";
    public static final String eventcomputeasyncfeatures = "Compute Asynchrounous Features";
    public static final String scoringengine = "Applying Artifical Intelligence: ";
}
