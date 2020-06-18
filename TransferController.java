package com.bas.backend.controller;

import com.bas.backend.beans.*;
import com.bas.backend.repository.TransferRepository;
import com.bas.config.KafkaConfig;
import com.bas.tools.KafkaTools;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/efraud")
public class TransferController {

    @Autowired
    PropertiesBackend propertiesBackend;

    @Autowired
    @Qualifier("transferRepository")
    private TransferRepository transferRepository;


    @CrossOrigin
    @GetMapping("/simulation")
    public void getAllTransfer(@RequestParam(name = "rule") String rule, @RequestParam(name = "data") String data) {

    }


    @CrossOrigin
    @RequestMapping(value = "/addScoringModel", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addNewScoringModel(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("enable") Boolean enable,
            @RequestParam("files") MultipartFile[] uploadFiles
    ) throws Exception {
        String strcontent = "";
        for (MultipartFile file: uploadFiles) {
            ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
            strcontent = IOUtils.toString(stream, "UTF-8");
        }
        ScoringBackend scoringBackend = new ScoringBackend();
        scoringBackend.name = name;
        scoringBackend.description = description;
        if(enable)
            scoringBackend.enable = "Y";
        else
            scoringBackend.enable = "N";
        scoringBackend.content = strcontent;

        transferRepository.addNewScoring(scoringBackend);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/getScoringById")
    public ResponseEntity<ScoringBackend> getScoringById() {
        return ResponseEntity.ok(transferRepository.getScoringById(3));
    }

    @CrossOrigin
    @GetMapping("/getAllScoring")
    public ResponseEntity<Scorings> getAllScoring() {
        return ResponseEntity.ok(transferRepository.getAllScoring());
    }


    @CrossOrigin
    @RequestMapping(value = "/modifyScoringModel", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> modifyScoringModel(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("enable") Boolean enable,
            @RequestParam("files") MultipartFile[] uploadFiles
    ) throws Exception {
        String strcontent = "";
        for (MultipartFile file: uploadFiles) {
            ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
            strcontent = IOUtils.toString(stream, "UTF-8");
        }
        ScoringBackend scoringBackend = new ScoringBackend();
        scoringBackend.id = id;
        scoringBackend.name = name;
        scoringBackend.description = description;
        System.out.println("=====> " + enable);
        if(enable)
            scoringBackend.enable = "Y";
        else
            scoringBackend.enable = "N";
        scoringBackend.content = strcontent;

        transferRepository.modifyScoring(scoringBackend);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/enableAllScorings")
    public ResponseBackend enableAllScorings() {
        ResponseEntity<Integer> ret = ResponseEntity.ok(transferRepository.enableAllScorings());

        ResponseBackend responseBackend = new ResponseBackend();
        responseBackend.statusCode = ret.getStatusCode();
        responseBackend.statusCodeValue = ret.getStatusCodeValue();
        responseBackend.body = ret.getBody();

        if (responseBackend.statusCodeValue == 200) {
            responseBackend.message = "All Scoring Models are enabled succefully (Total: " + responseBackend.body + " models)";
        } else {
            responseBackend.message = "Failed to enable All Scoring Models";
        }

        return responseBackend;
    }

    @CrossOrigin
    @GetMapping("/deployAllScorings")
    public ResponseBackend deployAllScorings() {

        ResponseBackend responseBackend = new ResponseBackend();

        Scorings scorings = transferRepository.getAllScoring();
        System.out.println(scorings.toString());

        KafkaConfig kafkaConfig = KafkaTools.getKafkaProducerProperties(propertiesBackend.getConfigurationfile(), propertiesBackend.getConfigscorings());
        KafkaTools.publishToKafka(KafkaTools.createKafkaProducer(kafkaConfig), kafkaConfig.topic, scorings.toString());

        responseBackend.statusCodeValue = 200;
        responseBackend.message = "SCoring Models deployed successfully (Total:" + scorings.scoringcount + " scoring models)";

        return responseBackend;
    }

    @CrossOrigin
    @GetMapping("/getAllTransfer")
    public ResponseEntity<Transactions> getAllTransfer(@RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit) {
        return ResponseEntity.ok(transferRepository.getValidatedTransactions(offset, limit, null));
    }

    @CrossOrigin
    @GetMapping("/getBadTransfer")
    public ResponseEntity<Transactions> getBadTransfer(@RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit) {
        return ResponseEntity.ok(transferRepository.getBadTransactions(offset, limit, null));
    }

    @CrossOrigin
    @GetMapping("/getTransferByID")
    public ResponseEntity<TransactionBackend> getTransferByID(@RequestParam(name = "transferid") String transferid) {
        return ResponseEntity.ok(transferRepository.getTransactionByID(transferid));
    }

    @CrossOrigin
    @RequestMapping(value = "/searchTransaction", method = RequestMethod.POST)
    public ResponseEntity<Transactions> searchTransaction(@RequestBody SearchCriteria searchCriteria, @RequestParam(name = "offset") Integer offset,
                                                          @RequestParam(name = "limit") Integer limit) {
        return ResponseEntity.ok(transferRepository.getValidatedTransactions(offset, limit, searchCriteria));
    }

    @CrossOrigin
    @RequestMapping(value = "/saveSearchTransaction", method = RequestMethod.POST)
    public ResponseEntity<Integer> saveSearchTransaction(@RequestBody SearchCriteria searchCriteria) {
        return ResponseEntity.ok(transferRepository.saveSearchTransaction(searchCriteria));
    }

    @CrossOrigin
    @GetMapping(value = "/getAllSearchProfiles")
    public ResponseEntity<List<SearchCriteria>> getAllSearchProfiles() {
        return ResponseEntity.ok(transferRepository.getAllSearchProfiles());
    }

    @CrossOrigin
    @GetMapping("/getSearchProfileByID")
    public ResponseEntity<SearchCriteria> getSearchProfileByID(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(transferRepository.getSearchProfileById(id));
    }

    @CrossOrigin
    @GetMapping(value = "/getAllTransactionsOfSearchProfiles")
    public ResponseEntity<Transactions> getAllTransactionsOfSearchProfiles(@RequestParam(name = "idprofile") Long idprofile, @RequestParam(name = "offset") Integer offset,
                                                                           @RequestParam(name = "limit") Integer limit) {
        System.out.println("====> " + idprofile);
        return ResponseEntity.ok(transferRepository.getAllTransactionsOfSearchProfiles(idprofile, offset, limit));
    }

    @CrossOrigin
    @RequestMapping(value = "/modifyRule", method = RequestMethod.POST)
    public ResponseEntity<Integer> modifyRule(@RequestBody Rule rule) {
        transferRepository.modifyRule(rule);
        return ResponseEntity.ok(200);
    }

    @CrossOrigin
    @RequestMapping(value = "/enableDisableRule", method = RequestMethod.POST)
    public ResponseEntity<Integer> enableDisableRule(@RequestBody Rule rule) {
        transferRepository.enableDisableRule(rule);
        return ResponseEntity.ok(200);
    }

    @CrossOrigin
    @RequestMapping(value = "/enableDisableModel", method = RequestMethod.POST)
    public ResponseEntity<Integer> enableDisableModel(@RequestBody ScoringBackend scoringBackend) {
        transferRepository.enableDisableScoring(scoringBackend);
        return ResponseEntity.ok(200);
    }

    @CrossOrigin
    @RequestMapping(value = "/addRule", method = RequestMethod.POST)
    public ResponseEntity<Integer> addRule(@RequestBody Rule rule) {
        transferRepository.addNewRule(rule);
        return ResponseEntity.ok(200);
    }

    @CrossOrigin
    @GetMapping("/getAllRules")
    public ResponseEntity<Rules> getAllRules() {
        return ResponseEntity.ok(transferRepository.getAllRules());
    }


    @CrossOrigin
    @GetMapping("/deployAllRules")
    public ResponseBackend deployAllRules() {

        ResponseBackend responseBackend = new ResponseBackend();

        Rules rules = transferRepository.getAllRules();

        KafkaConfig kafkaConfig = KafkaTools.getKafkaProducerProperties(propertiesBackend.getConfigurationfile(), propertiesBackend.getConfigrules());
        KafkaTools.publishToKafka(KafkaTools.createKafkaProducer(kafkaConfig), kafkaConfig.topic, rules.toString());

        responseBackend.statusCodeValue = 200;
        responseBackend.message = "Rules deployed successfully (Total:" + rules.rulesCount + " rules)";

        return responseBackend;
    }


    @CrossOrigin
    @GetMapping("/getAllFeatures")
    public ResponseEntity<Features> getAllFeatures() {
        return ResponseEntity.ok(transferRepository.getAllFeatures());
    }

    @CrossOrigin
    @GetMapping("/getFeatureByID")
    public ResponseEntity<FeatureBackend> getFeatureByID(@RequestParam(name = "featureid") Integer featureid) {
        return ResponseEntity.ok(transferRepository.getFeatureById(featureid));
    }

    @CrossOrigin
    @RequestMapping(value = "/addFeature", method = RequestMethod.POST)
    public ResponseEntity<Integer> addFeature(@RequestBody FeatureBackend featureBackend) {
        transferRepository.addNewFeature(featureBackend);
        return ResponseEntity.ok(200);
    }

    @CrossOrigin
    @RequestMapping(value = "/modifyFeature", method = RequestMethod.POST)
    public ResponseEntity<Integer> modifyFeature(@RequestBody FeatureBackend featureBackend) {
        transferRepository.modifyFeature(featureBackend);
        return ResponseEntity.ok(200);
    }

    @CrossOrigin
    @GetMapping("/enableAllFeatures")
    public ResponseBackend enableAllFeatures() {
        ResponseEntity<Integer> ret = ResponseEntity.ok(transferRepository.enableAllFeatures());

        ResponseBackend responseBackend = new ResponseBackend();
        responseBackend.statusCode = ret.getStatusCode();
        responseBackend.statusCodeValue = ret.getStatusCodeValue();
        responseBackend.body = ret.getBody();

        if (responseBackend.statusCodeValue == 200) {
            responseBackend.message = "All Features are enabled succefully (Total: " + responseBackend.body + " features)";
        } else {
            responseBackend.message = "Failed to enable All Features";
        }

        return responseBackend;
    }

    @CrossOrigin
    @GetMapping("/deployAllFeatures")
    public ResponseBackend deployAllFeatures() {

        ResponseBackend responseBackend = new ResponseBackend();

        Features features = transferRepository.getAllFeatures();

        KafkaConfig kafkaConfig = KafkaTools.getKafkaProducerProperties(propertiesBackend.getConfigurationfile(), propertiesBackend.getConfigfeatures());
        KafkaTools.publishToKafka(KafkaTools.createKafkaProducer(kafkaConfig), kafkaConfig.topic, features.toString());

        responseBackend.statusCodeValue = 200;
        responseBackend.message = "Features deployed successfully (Total:" + features.featuresCount + " features)";

        return responseBackend;
    }


    //Dashboard
    @CrossOrigin
    @GetMapping("/transferResponseTime")
    public List<TransactionResponseTime> transferResponseTime() {
        //List<TransferResponseTime> ret = new ArrayList<>();
/*

        List<String> dates = Arrays.asList(
                "2019-12-01 19:01:00","2019-12-01 19:02:00","2019-12-01 20:03:00","2019-12-01 20:04:00","2019-12-01 21:05:00","2019-12-01 21:06:00","2019-12-01 21:07:00","2019-12-01 22:08:00","2019-12-01 22:09:00","2019-12-01 23:10:00"
        );
        for (String string: dates) {
            Random r = new Random();
            LocalDateTime localDateTime = LocalDateTime.parse(string,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            TransferResponseTime transferResponseTime = new TransferResponseTime();
            transferResponseTime.x = localDateTime
                    .atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli();
            transferResponseTime.y1 = r.nextInt((386 - 126) + 1) + 126;
            ret.add(transferResponseTime);
        }
*/

        return transferRepository.getTransactionResponseTime();

    }


    @CrossOrigin
    @GetMapping("/getConfig")
    public JSONObject getFields() {

        String stringToParse =  "{" +
                "  \"amount\": {" +
                "    \"label\": \"Amount\"," +
                "    \"type\": \"number\"," +
                "    \"fieldSettings\": {" +
                "      \"min\": 0" +
                "    }," +
                "    \"valueSources\": [\"value\"]," +
                "    \"preferWidgets\": [\"number\"]" +
                "  }," +
                "  \"debtoraccount\": {" +
                "    \"label\": \"DebtorAccount\"," +
                "    \"type\": \"text\"," +
                "    \"valueSources\": [\"value\"]," +
                "    \"preferWidgets\": [\"text\"]" +
                "  }," +
                "  \"debtorbic\": {" +
                "    \"label\": \"DebtorBic\"," +
                "    \"type\": \"select\"," +
                "    \"valueSources\": [\"value\"]," +
                "    \"operators\": [\"select_any_in\"]," +
                "    \"listValues\": [" +
                "      { \"value\": \"SIMWF21\", \"title\": \"SIMWF21\" }," +
                "      { \"value\": \"SIMARERX\", \"title\": \"SIMARERX\" }," +
                "      { \"value\": \"SIMAMQMX\", \"title\": \"SIMAMQMX\" }," +
                "      { \"value\": \"SIMANCNX\", \"title\": \"SIMANCNX\" }," +
                "      { \"value\": \"SIMAMCM1\", \"title\": \"SIMAMCM1\" }" +
                "    ]" +
                "  }," +
                "  \"debtorcountry\": {" +
                "    \"label\": \"DebtorCountry\"," +
                "    \"type\": \"select\"," +
                "    \"valueSources\": [\"value\"]," +
                "    \"operators\": [\"select_any_in\"]," +
                "    \"listValues\": [" +
                "      { \"value\": \"BG\", \"title\": \"BG\" }," +
                "      { \"value\": \"CN\", \"title\": \"CN\" }," +
                "      { \"value\": \"CY\", \"title\": \"CY\" }," +
                "      { \"value\": \"EE\", \"title\": \"EE\" }," +
                "      { \"value\": \"GR\", \"title\": \"GR\" }," +
                "      { \"value\": \"LT\", \"title\": \"LT\" }," +
                "      { \"value\": \"LV\", \"title\": \"LV\" }," +
                "      { \"value\": \"PL\", \"title\": \"PL\" }," +
                "      { \"value\": \"GE\", \"title\": \"GE\" }," +
                "      { \"value\": \"HR\", \"title\": \"HR\" }," +
                "      { \"value\": \"FR\", \"title\": \"FR\" }" +
                "    ]" +
                "  }," +
                "  \"creditoraccount\": {" +
                "    \"label\": \"CreditorAccount\"," +
                "    \"type\": \"text\"," +
                "    \"valueSources\": [\"value\"]," +
                "    \"preferWidgets\": [\"text\"]" +
                "  }," +
                "  \"creditorbic\": {" +
                "    \"label\": \"CreditorBic\"," +
                "    \"type\": \"select\"," +
                "    \"valueSources\": [\"value\"]," +
                "    \"operators\": [\"select_any_in\"]," +
                "    \"listValues\": [" +
                "      { \"value\": \"SIMWF21\", \"title\": \"SIMWF21\" }," +
                "      { \"value\": \"SIMARERX\", \"title\": \"SIMARERX\" }," +
                "      { \"value\": \"SIMAMQMX\", \"title\": \"SIMAMQMX\" }," +
                "      { \"value\": \"SIMANCNX\", \"title\": \"SIMANCNX\" }," +
                "      { \"value\": \"SIMAMCM1\", \"title\": \"SIMAMCM1\" }" +
                "    ]" +
                "  }," +
                "  \"creditorcountry\": {" +
                "    \"label\": \"creditorCountry\"," +
                "    \"type\": \"select\"," +
                "    \"valueSources\": [\"value\"]," +
                "    \"operators\": [\"select_any_in\"]," +
                "    \"listValues\": [" +
                "      { \"value\": \"BG\", \"title\": \"BG\" }," +
                "      { \"value\": \"CN\", \"title\": \"CN\" }," +
                "      { \"value\": \"CY\", \"title\": \"CY\" }," +
                "      { \"value\": \"EE\", \"title\": \"EE\" }," +
                "      { \"value\": \"GR\", \"title\": \"GR\" }," +
                "      { \"value\": \"LT\", \"title\": \"LT\" }," +
                "      { \"value\": \"LV\", \"title\": \"LV\" }," +
                "      { \"value\": \"PL\", \"title\": \"PL\" }," +
                "      { \"value\": \"GE\", \"title\": \"GE\" }," +
                "      { \"value\": \"HR\", \"title\": \"HR\" }," +
                "      { \"value\": \"FR\", \"title\": \"FR\" }" +
                "    ]" +
                "  }" +
                "}";
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(stringToParse);
            return json;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;


    }


    @CrossOrigin
    @GetMapping("/getInit")
    public JSONObject getInit() {

        String stringToParse =  "{" +
                "  \"children1\": {" +
                "    \"988a9a8b-0123-4456-b89a-b16ff5ebb6e1\": {" +
                "      \"type\": \"rule\"," +
                "      \"properties\": {" +
                "        \"field\": \"amount\"," +
                "        \"operator\": \"equal\"," +
                "        \"value\": [" +
                "          12000" +
                "        ]," +
                "        \"valueSrc\": [" +
                "          \"value\"" +
                "        ]," +
                "        \"valueType\": [" +
                "          \"number\"" +
                "        ]" +
                "      }" +
                "    }," +
                "    \"abab8999-cdef-4012-b456-716ff5ebdd60\": {" +
                "      \"type\": \"rule\"," +
                "      \"properties\": {" +
                "        \"field\": \"creditorcountry\"," +
                "        \"operator\": \"select_any_in\"," +
                "        \"value\": [" +
                "          [" +
                "            \"CN\"" +
                "          ]" +
                "        ]," +
                "        \"valueSrc\": [" +
                "          \"value\"" +
                "        ]," +
                "        \"valueType\": [" +
                "          \"multiselect\"" +
                "        ]" +
                "      }" +
                "    }" +
                "  }," +
                "  \"id\": \"9abb9a8b-89ab-4cde-b012-316ff0b452ee\"," +
                "  \"type\": \"group\"," +
                "  \"properties\": {" +
                "    \"conjunction\": \"AND\"" +
                "  }" +
                "}";
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(stringToParse);
            return json;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;


    }

}
