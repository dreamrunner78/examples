package com.bas.backend.repository;

import com.bas.backend.beans.*;
import com.bas.constants.Params;
import com.bas.pivot.ValidationBean;
import com.bas.tools.ToolsDate;
import com.bas.tools.ToolsMath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

@Component
@Qualifier("transferRepository")
public class JdbcTransferRepository implements TransferRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static String convertToSingleChar(String input) {
        if ("true".equalsIgnoreCase(input))
            return "Y";
        return "N";
    }

    @Override
    public int count(String criteria) {
        return jdbcTemplate.queryForObject("select count(*) from transaction " + criteria, Integer.class);
    }

    public Transactions getListTransactions(Integer offset, Integer limit, String validated, SearchCriteria searchCriteria) {
        String sqlQuery = "select * from transaction ";

        //String criteria = "where validated='" + validated + "' and decision='BLOCK'";
        String criteria = "where validated='" + validated + "'";

        if(searchCriteria != null) {

            if (searchCriteria.decision != null && !"".equals(searchCriteria.decision)) {
                criteria += " and UPPER(decision) = '" + searchCriteria.decision.toUpperCase() + "'";
            } else {
                criteria += " and ( UPPER(decision) = 'BLOCK' OR UPPER(decision) = 'PASS' )";
            }

            if (searchCriteria.rangeDate != null && searchCriteria.rangeDate.length == 2) {
                String tmpinf = ToolsDate.inferFormatAndConvertToNewFormat(searchCriteria.rangeDate[0], "yyyy-MM-dd");
                String tmpsup = ToolsDate.inferFormatAndConvertToNewFormat(searchCriteria.rangeDate[1], "yyyy-MM-dd");
                if (tmpinf.equalsIgnoreCase(tmpsup)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    tmpsup = sdf.format(ToolsDate.convertLocalDateTimeToDate(ToolsDate.convertToLocalDateTime(ToolsDate.inferFormatAndConvertToNewFormatDate(tmpinf, "yyyy-MM-dd HH:mm:ss")).plusDays(1l)));
                }
                String dateInf = ToolsDate.inferFormatAndConvertToNewFormat(tmpinf, "yyyy-MM-dd HH:mm:ss");
                criteria += " and settlementdate >= TO_TIMESTAMP('" + dateInf + "', 'yyyy-MM-dd HH24:mi:ss')";
                String dateSup = ToolsDate.inferFormatAndConvertToNewFormat(tmpsup, "yyyy-MM-dd HH:mm:ss");
                criteria += " and settlementdate <= TO_TIMESTAMP('" + dateSup + "', 'yyyy-MM-dd HH24:mi:ss')";
            }

            /*if (searchCriteria.decision != null && !"".equals(searchCriteria.decision))
                criteria += " and UPPER(decision) = '" + searchCriteria.decision.toUpperCase() + "'";

            if ("".equals(searchCriteria.decision))
                criteria += " and (UPPER(decision) = 'BLOCK' OR UPPER(decision) = 'PASS')";
            */

            if (searchCriteria.debtorAccount != null && !"".equals(searchCriteria.debtorAccount))
                criteria += " and debtoraccount like '%" + searchCriteria.debtorAccount.toUpperCase() + "%'";

            if (searchCriteria.debtorName != null && !"".equals(searchCriteria.debtorName))
                criteria += " and UPPER(debtorname) like '%" + searchCriteria.debtorName.toUpperCase() + "%'";

            if (searchCriteria.debtorBic != null && !"".equals(searchCriteria.debtorBic))
                criteria += " and UPPER(debtorbic) like '%" + searchCriteria.debtorBic.toUpperCase() + "%'";

            if (searchCriteria.debtorCountry != null && !"".equals(searchCriteria.debtorCountry))
                criteria += " and UPPER(debtorcountry) like '%" + searchCriteria.debtorCountry.toUpperCase() + "%'";

            if (searchCriteria.creditorAccount != null && !"".equals(searchCriteria.creditorAccount))
                criteria += " and creditoraccount like '%" + searchCriteria.creditorAccount.toUpperCase() + "%'";

            if (searchCriteria.creditorName != null && !"".equals(searchCriteria.creditorName))
                criteria += " and UPPER(creditorname) like '%" + searchCriteria.creditorName.toUpperCase() + "%'";

            if (searchCriteria.creditorBic != null && !"".equals(searchCriteria.creditorBic))
                criteria += " and UPPER(creditorbic) like '%" + searchCriteria.creditorBic.toUpperCase() + "%'";

            if (searchCriteria.creditorCountry != null && !"".equals(searchCriteria.creditorCountry))
                criteria += " and UPPER(creditorcountry) like '%" + searchCriteria.creditorCountry.toUpperCase() + "%'";

            if (searchCriteria.amountInf != null && ToolsMath.isDouble(String.valueOf(searchCriteria.amountInf)))
                criteria += " and amount >=" + searchCriteria.amountInf;

            if (searchCriteria.amountSup != null && ToolsMath.isDouble(String.valueOf(searchCriteria.amountSup)))
                criteria += " and amount <=" + searchCriteria.amountSup;

            if (searchCriteria.transferType != null && !"".equals(searchCriteria.transferType))
                criteria += " and UPPER(transfertype) = '" + searchCriteria.transferType.toUpperCase() + "'";
        }
        else {
            criteria += " and UPPER(decision) = 'BLOCK'";

            String tmpsup = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tmpinf = sdf.format(ToolsDate.convertLocalDateTimeToDate(ToolsDate.convertToLocalDateTime(ToolsDate.inferFormatAndConvertToNewFormatDate(tmpsup, "yyyy-MM-dd HH:mm:ss")).minusDays(7l)));

            String dateInf = ToolsDate.inferFormatAndConvertToNewFormat(tmpinf, "yyyy-MM-dd HH:mm:ss");
            criteria += " and settlementdate >= TO_TIMESTAMP('" + dateInf + "', 'yyyy-MM-dd HH24:mi:ss')";

            tmpsup = sdf.format(ToolsDate.convertLocalDateTimeToDate(ToolsDate.convertToLocalDateTime(ToolsDate.inferFormatAndConvertToNewFormatDate(tmpsup, "yyyy-MM-dd HH:mm:ss")).plusDays(1l)));
            String dateSup = ToolsDate.inferFormatAndConvertToNewFormat(tmpsup, "yyyy-MM-dd HH:mm:ss");
            criteria += " and settlementdate <= TO_TIMESTAMP('" + dateSup + "', 'yyyy-MM-dd HH24:mi:ss')";
        }

        sqlQuery += criteria + " offset " + String.valueOf(offset) + " ROWS FETCH NEXT " + String.valueOf(limit) + " ROWS ONLY ";
        String finalCriteria = criteria;

        //System.out.println(sqlQuery);

        return getTransactionsFromSqlQuery(sqlQuery, finalCriteria);

    }

    public Transactions getTransactionsFromSqlQuery(String sqlQuery, String finalCriteria) {
        return jdbcTemplate.query(sqlQuery,
                new ResultSetExtractor<Transactions>() {
                    List<TransactionBackend> list_transactionBackend = new ArrayList<>();

                    @Override
                    public Transactions extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                        while (resultSet.next()) {
                            TransactionBackend transactionBackend = new TransactionBackend();
                            transactionBackend.transferId = resultSet.getString("transferid");
                            transactionBackend.transferReference = resultSet.getString("transferreference");
                            transactionBackend.transferType = resultSet.getString("transfertype");
                            transactionBackend.debtorAccount = resultSet.getString("debtorAccount");
                            transactionBackend.creditorAccount = resultSet.getString("creditorAccount");
                            transactionBackend.amount = resultSet.getDouble("amount");
                            transactionBackend.currency = resultSet.getString("currency");
                            transactionBackend.decision = resultSet.getString("decision");
                            transactionBackend.duration = resultSet.getLong("duration");
                            transactionBackend.errorcompute = resultSet.getString("errorcompute");
                            try {
                                JSONParser jsonParser = new JSONParser();
                                Blob blobValidationBeans = resultSet.getBlob("validationBeans");
                                StringBuffer tmpvalidationBeans = new StringBuffer();
                                String auxValidationBeans;
                                BufferedReader brValidationBeans = new BufferedReader(new InputStreamReader(blobValidationBeans.getBinaryStream()));
                                while ((auxValidationBeans = brValidationBeans.readLine()) != null) {
                                    tmpvalidationBeans.append(auxValidationBeans);
                                }
                                if (tmpvalidationBeans != null && !"".equals(tmpvalidationBeans.toString())) {
                                    JSONArray rootObject = (JSONArray) jsonParser.parse(new StringReader(tmpvalidationBeans.toString()));
                                    for (Object iter : rootObject) {
                                        JSONObject jsonObject = (JSONObject) iter;
                                        transactionBackend.validationBeans.add(
                                                new ValidationBean(
                                                        (String) jsonObject.get("field"),
                                                        (Boolean) jsonObject.get("status"),
                                                        (String) jsonObject.get("message")
                                                )
                                        );
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            list_transactionBackend.add(transactionBackend);

                        }
                        Transactions transactions = new Transactions();
                        transactions.transfersCount = count(finalCriteria);
                        transactions.list_transactionBackend = list_transactionBackend;
                        return transactions;
                    }
                });
    }

    @Override
    public Transactions getValidatedTransactions(Integer offset, Integer limit, SearchCriteria searchCriteria) {
        return getListTransactions(offset, limit, "Y", searchCriteria);
    }

    @Override
    public int saveSearchTransaction(SearchCriteria searchCriteria) {
        String criteria = "where validated='Y' ";

        Boolean found = false;

        if (searchCriteria != null) {
            if (searchCriteria.rangeDate != null && searchCriteria.rangeDate.length == 2) {
                found = true;

                String tmpinf = ToolsDate.inferFormatAndConvertToNewFormat(searchCriteria.rangeDate[0], "yyyy-MM-dd");
                String tmpsup = ToolsDate.inferFormatAndConvertToNewFormat(searchCriteria.rangeDate[1], "yyyy-MM-dd");

                if (tmpinf.equalsIgnoreCase(tmpsup)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    tmpsup = sdf.format(ToolsDate.convertLocalDateTimeToDate(ToolsDate.convertToLocalDateTime(ToolsDate.inferFormatAndConvertToNewFormatDate(tmpinf, "yyyy-MM-dd HH:mm:ss")).plusDays(1l)));
                }

                String dateInf = ToolsDate.inferFormatAndConvertToNewFormat(tmpinf, "yyyy-MM-dd HH:mm:ss");
                criteria += " and settlementdate >= TO_TIMESTAMP('" + dateInf + "', 'yyyy-MM-dd HH24:mi:ss')";

                String dateSup = ToolsDate.inferFormatAndConvertToNewFormat(tmpsup, "yyyy-MM-dd HH:mm:ss");
                criteria += " and settlementdate <= TO_TIMESTAMP('" + dateSup + "', 'yyyy-MM-dd HH24:mi:ss')";

            }

            if (searchCriteria.decision != null && !"".equals(searchCriteria.decision)) {
                found = true;
                criteria += " and UPPER(decision) = '" + searchCriteria.decision.toUpperCase() + "'";
            }

            if (searchCriteria.debtorAccount != null && !"".equals(searchCriteria.debtorAccount)) {
                found = true;
                criteria += " and debtoraccount like '%" + searchCriteria.debtorAccount.toUpperCase() + "%'";
            }

            if (searchCriteria.debtorName != null && !"".equals(searchCriteria.debtorName)) {
                found = true;
                criteria += " and UPPER(debtorname) like '%" + searchCriteria.debtorName.toUpperCase() + "%'";
            }

            if (searchCriteria.debtorBic != null && !"".equals(searchCriteria.debtorBic)) {
                found = true;
                criteria += " and UPPER(debtorbic) like '%" + searchCriteria.debtorBic.toUpperCase() + "%'";
            }

            if (searchCriteria.debtorCountry != null && !"".equals(searchCriteria.debtorCountry)) {
                found = true;
                criteria += " and UPPER(debtorcountry) like '%" + searchCriteria.debtorCountry.toUpperCase() + "%'";
            }

            if (searchCriteria.creditorAccount != null && !"".equals(searchCriteria.creditorAccount)) {
                criteria += " and creditoraccount like '%" + searchCriteria.creditorAccount.toUpperCase() + "%'";
            }

            if (searchCriteria.creditorName != null && !"".equals(searchCriteria.creditorName)) {
                found = true;
                criteria += " and UPPER(creditorname) like '%" + searchCriteria.creditorName.toUpperCase() + "%'";
            }

            if (searchCriteria.creditorBic != null && !"".equals(searchCriteria.creditorBic)) {
                found = true;
                criteria += " and UPPER(creditorbic) like '%" + searchCriteria.creditorBic.toUpperCase() + "%'";
            }

            if (searchCriteria.creditorCountry != null && !"".equals(searchCriteria.creditorCountry)) {
                found = true;
                criteria += " and UPPER(creditorcountry) like '%" + searchCriteria.creditorCountry.toUpperCase() + "%'";
            }

            if (searchCriteria.amountInf != null && ToolsMath.isDouble(String.valueOf(searchCriteria.amountInf))) {
                found = true;
                criteria += " and amount >=" + searchCriteria.amountInf;
            }

            if (searchCriteria.amountSup != null && ToolsMath.isDouble(String.valueOf(searchCriteria.amountSup))) {
                found = true;
                criteria += " and amount <=" + searchCriteria.amountSup;
            }

            if (searchCriteria.transferType != null && !"".equals(searchCriteria.transferType)) {
                found = true;
                criteria += " and UPPER(transfertype) = '" + searchCriteria.transferType.toUpperCase() + "'";
            }


            if (found)
                return jdbcTemplate.update("insert into searchprofiles(name, description,content) values(?,?,?)", searchCriteria.name, searchCriteria.description, criteria);

        }
        return 0;
    }

    @Override
    public List<SearchCriteria> getAllSearchProfiles() {

        return jdbcTemplate.query("select id,name from searchprofiles",
                new ResultSetExtractor<List<SearchCriteria>>() {
                    List<SearchCriteria> list_searchProfiles = new ArrayList<>();

                    @Override
                    public List<SearchCriteria> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        while (resultSet.next()) {
                            SearchCriteria searchCriteria = new SearchCriteria();
                            searchCriteria.id = resultSet.getLong("id");
                            searchCriteria.name = resultSet.getString("name");
                            list_searchProfiles.add(searchCriteria);
                        }
                        return list_searchProfiles;
                    }
                });
    }

    @Override
    public SearchCriteria getSearchProfileById(Long id) {
        return jdbcTemplate.queryForObject("select * from searchprofiles where id = ?", new Object[]{id}, new RowMapper<SearchCriteria>() {
            @Override
            public SearchCriteria mapRow(ResultSet resultSet, int i) throws SQLException {
                SearchCriteria searchCriteria = new SearchCriteria();
                searchCriteria.id = resultSet.getLong("id");
                searchCriteria.name = resultSet.getString("name");
                searchCriteria.description = resultSet.getString("description");
                searchCriteria.content = resultSet.getString("content");
                return searchCriteria;
            }
        });
    }

    @Override
    public Transactions getAllTransactionsOfSearchProfiles(Long id, Integer offset, Integer limit) {
        SearchCriteria searchCriteria = getSearchProfileById(id);

        String sqlQuery = "select * from transaction " + searchCriteria.content + " offset " + String.valueOf(offset) + " ROWS FETCH NEXT " + String.valueOf(limit) + " ROWS ONLY ";

        Transactions transactions = getTransactionsFromSqlQuery(sqlQuery, searchCriteria.content);

        return transactions;
    }

    @Override
    public Transactions getBadTransactions(Integer offset, Integer limit, SearchCriteria searchCriteria) {

        System.out.println("BAD TRANSACTION");
        String sqlQuery = "select * from transaction ";

        String criteria = "where validated='N' ";
        sqlQuery += criteria;
        return getTransactionsFromSqlQuery(sqlQuery, criteria);

        //return getListTransactions(offset, limit, "N", searchCriteria);
    }

    @Override
    public TransactionBackend getTransactionByID(String transferid) {
        TransactionBackend transactionBackend = new TransactionBackend();

        Map<Long, Rule> mapRules = new HashMap<>();
        List<Rule> listAllRules = getAllRules().list_rules;
        for (Rule rule: listAllRules) {
            mapRules.put(rule.id, rule);
        }
        
        return jdbcTemplate.queryForObject(
                "select *  from transaction where transferid=?",
                new Object[]{transferid}, new RowMapper<TransactionBackend>() {
                    @Override
                    public TransactionBackend mapRow(ResultSet resultSet, int i) throws SQLException {

                        transactionBackend.transferId = resultSet.getString("transferId");
                        transactionBackend.transferReference = resultSet.getString("transferReference");
                        transactionBackend.transferType = resultSet.getString("transferType");
                        transactionBackend.messageType = resultSet.getString("messageType");
                        transactionBackend.userData = resultSet.getString("userData");
                        transactionBackend.rawMessage = resultSet.getString("rawMessage");
                        transactionBackend.originSystem = resultSet.getString("originSystem");


                        transactionBackend.settlementDate = ToolsDate.inferFormatAndConvertToNewFormat(resultSet.getString("settlementDate"), "dd-MM-yyyy HH:mm:ss");

                        transactionBackend.debtorAccount = resultSet.getString("debtorAccount");
                        transactionBackend.debtorBic = resultSet.getString("debtorBic");
                        transactionBackend.debtorCountry = resultSet.getString("debtorCountry");
                        transactionBackend.debtorName = resultSet.getString("debtorName");
                        transactionBackend.creditorAccount = resultSet.getString("creditorAccount");
                        transactionBackend.creditorBic = resultSet.getString("creditorBic");
                        transactionBackend.creditorCountry = resultSet.getString("creditorCountry");
                        transactionBackend.creditorName = resultSet.getString("creditorName");
                        transactionBackend.amount = resultSet.getDouble("amount");
                        transactionBackend.currency = resultSet.getString("currency");
                        transactionBackend.decision = resultSet.getString("decision");

                        JSONParser jsonParser = new JSONParser();
                        try {

                            Blob blobStep = resultSet.getBlob("steps");
                            StringBuffer tmpsteps = new StringBuffer();
                            String auxSteps;
                            BufferedReader brSteps = new BufferedReader(new InputStreamReader(blobStep.getBinaryStream()));
                            while ((auxSteps = brSteps.readLine()) != null) {
                                tmpsteps.append(auxSteps);
                            }
                            if (tmpsteps != null && !"".equals(tmpsteps)) {
                                JSONArray rootObject = (JSONArray) jsonParser.parse(new StringReader(tmpsteps.toString()));
                                for (Object iter : rootObject) {
                                    JSONObject jsonObject = (JSONObject) iter;
                                    transactionBackend.steps.add(
                                            new StepBackend(
                                                    (String) jsonObject.get("label"),
                                                    String.valueOf(new Timestamp((Long) jsonObject.get("starttime"))),
                                                    String.valueOf(new Timestamp((Long) jsonObject.get("endtime"))),
                                                    (Long) jsonObject.get("localduration"),
                                                    String.valueOf(new Timestamp((Long) jsonObject.get("endtimeoflastoperator"))),
                                                    (Long) jsonObject.get("durationfromlastoperator")
                                            )
                                    );
                                }
                            }


                            Blob blobScorings = resultSet.getBlob("scorings");
                            StringBuffer tmpScorings = new StringBuffer();
                            String auxScoring;
                            BufferedReader brScorings = new BufferedReader(new InputStreamReader(blobScorings.getBinaryStream()));
                            while ((auxScoring = brScorings.readLine()) != null) {
                                tmpScorings.append(auxScoring);
                            }
                            if (tmpScorings != null && !"".equals(tmpScorings)) {
                                JSONArray rootObject = (JSONArray) jsonParser.parse(new StringReader(tmpScorings.toString()));
                                for (Object iter : rootObject) {
                                    JSONObject jsonObject = (JSONObject) iter;
                                    transactionBackend.scoringBackends.add(
                                            new ScoringBackend(
                                                    (Long) jsonObject.get("id"),
                                                    (String) jsonObject.get("name"),
                                                    (String) jsonObject.get("description"),
                                                    Math.toIntExact((Long) jsonObject.get("result")),
                                                    (Double) jsonObject.get("probability0"),
                                                    (Double) jsonObject.get("probability1"),
                                                    (Long) jsonObject.get("startTime"),
                                                    (Long) jsonObject.get("endTime"),
                                                    (Long) jsonObject.get("duration")
                                            )
                                    );
                                }
                            }


                            Blob blobDecision = resultSet.getBlob("decisions");
                            StringBuffer tmpdecisions = new StringBuffer();
                            String auxDecisions;
                            BufferedReader brDecisions = new BufferedReader(new InputStreamReader(blobDecision.getBinaryStream()));
                            while ((auxDecisions = brDecisions.readLine()) != null) {
                                tmpdecisions.append(auxDecisions);
                            }
                            if (tmpdecisions != null && !"".equals(tmpdecisions)) {
                                JSONArray rootObject = (JSONArray) jsonParser.parse(new StringReader(tmpdecisions.toString()));
                                for (Object iter : rootObject) {
                                    JSONObject jsonObject = (JSONObject) iter;
                                    transactionBackend.decisions.add(
                                            new Decision(
                                                    (Long) jsonObject.get("idrule"),
                                                    (String) jsonObject.get("result"),
                                                    (String) jsonObject.get("startTime"),
                                                    (String) jsonObject.get("endTime"),
                                                    (String) jsonObject.get("duration"),
                                                    mapRules.get((Long) jsonObject.get("idrule"))
                                            )
                                    );
                                }
                            }

                            Blob blobValidationBeans = resultSet.getBlob("validationBeans");
                            StringBuffer tmpvalidationBeans = new StringBuffer();
                            String auxValidationBeans;
                            BufferedReader brValidationBeans = new BufferedReader(new InputStreamReader(blobValidationBeans.getBinaryStream()));
                            while ((auxValidationBeans = brValidationBeans.readLine()) != null) {
                                tmpvalidationBeans.append(auxValidationBeans);
                            }
                            if (tmpvalidationBeans != null && !"".equals(tmpvalidationBeans)) {
                                JSONArray rootObject = (JSONArray) jsonParser.parse(new StringReader(tmpvalidationBeans.toString()));
                                for (Object iter : rootObject) {
                                    JSONObject jsonObject = (JSONObject) iter;
                                    transactionBackend.validationBeans.add(
                                            new ValidationBean(
                                                    (String) jsonObject.get("field"),
                                                    (Boolean) jsonObject.get("status"),
                                                    (String) jsonObject.get("message")
                                            )
                                    );
                                }
                            }

                            //Fill list of features
                            List<String> listFeaturesType = Params.featuresType;
                            Blob blobFeatures = resultSet.getBlob("features");
                            StringBuffer tmpFeaturesStrBuffer = new StringBuffer();
                            String aux;
                            BufferedReader brFeatures = new BufferedReader(new InputStreamReader(blobFeatures.getBinaryStream()));
                            while ((aux = brFeatures.readLine()) != null) {
                                tmpFeaturesStrBuffer.append(aux);
                            }
                            JSONObject objectFeatures = (JSONObject) jsonParser.parse(new StringReader(tmpFeaturesStrBuffer.toString()));
                            for (String featureType : listFeaturesType) {
                                JSONArray tmpFeatures = (JSONArray) objectFeatures.get(featureType);
                                if (tmpFeatures != null) {
                                    for (int l = 0; l < tmpFeatures.size(); l++) {
                                        JSONObject jsonObject = (JSONObject) tmpFeatures.get((l));
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                        String strStartTime = "";
                                        String strEndTime = "";
                                        //Compute startTime and endTime in case of enabled feature
                                        if ("Y".equalsIgnoreCase((String) jsonObject.get("enable"))) {
                                            strStartTime = sdf.format(new Date((Long) jsonObject.get("starttime")));
                                            strEndTime = sdf.format(new Date((Long) jsonObject.get("endtime")));
                                        }
                                        transactionBackend.listFeatures.add(
                                                new FeatureBackend(
                                                        (Long) jsonObject.get("id"),
                                                        (String) jsonObject.get("name"),
                                                        (String) jsonObject.get("description"),
                                                        (String) jsonObject.get("classname"),
                                                        (String) jsonObject.get("transfertype"),
                                                        (String) jsonObject.get("enable"),
                                                        (String) jsonObject.get("fixed"),
                                                        String.valueOf((Double) jsonObject.get("value")),
                                                        (String) jsonObject.get("computationtype"),
                                                        (String) jsonObject.get("featuretype"),
                                                        strStartTime,
                                                        strEndTime,
                                                        String.valueOf((Long) jsonObject.get("duration")),
                                                        (String) jsonObject.get("rule"),
                                                        "", "",
                                                        (String) jsonObject.get("message")
                                                )
                                        );
                                    }
                                }
                            }
                            if (transactionBackend.listFeatures != null)
                                transactionBackend.featuresCount = transactionBackend.listFeatures.size();


                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //System.out.println(transactionBackend.toString());
                        return transactionBackend;
                    }
                });
    }

    @Override
    public int addNewRule(Rule rule) {
        /*return jdbcTemplate.update(
                "insert into rule(name, description, enable, type, content, tree) values(?,?,?,?,?,?)",
                rule.name, rule.description, convertToSingleChar(rule.enable), rule.type, rule.content, rule.tree);*/

        String[] linesContent = rule.content.split(System.getProperty("line.separator"));

        StringBuffer stringBufferContent = new StringBuffer();
        for (int i = 0; i < linesContent.length; i++) {
            stringBufferContent.append(linesContent[i]);
            stringBufferContent.append(System.getProperty("line.separator"));
        }

        String[] linesTree = rule.tree.split(System.getProperty("line.separator"));
        StringBuffer stringBufferTree = new StringBuffer();
        for (int i = 0; i < linesTree.length; i++) {
            stringBufferTree.append(linesTree[i]);
            stringBufferTree.append(System.getProperty("line.separator"));
        }
        String sqlinsert = "insert into rule(name, description, enable, type, content, tree) values(?,?,?,?,?,?)";
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlinsert);
                ps.setString(1, rule.name);
                ps.setString(2, rule.description);
                ps.setString(3, convertToSingleChar(rule.enable));
                ps.setString(4, rule.type);

                byte[] blobContent = stringBufferContent.toString().getBytes();
                ps.setBinaryStream(5, new ByteArrayInputStream(blobContent));

                byte[] blobTree = stringBufferTree.toString().getBytes();
                ps.setBinaryStream(6, new ByteArrayInputStream(blobTree));
                return ps;
            }
        });

    }

    @Override
    public int modifyRule(Rule rule) {
        /*return jdbcTemplate.update(
                "update rule set name=?, description=?, enable=?, type=?, content=?, tree=? where id=?",
                rule.name, rule.description, convertToSingleChar(rule.enable), rule.type, rule.content, rule.tree, rule.id);*/

        String[] linesContent = rule.content.split(System.getProperty("line.separator"));
        String[] linesTree = rule.tree.split(System.getProperty("line.separator"));
        StringBuffer stringBufferContent = new StringBuffer();
        for (int i = 0; i < linesContent.length; i++) {
            stringBufferContent.append(linesContent[i]);
            stringBufferContent.append(System.getProperty("line.separator"));
        }

        StringBuffer stringBufferTree = new StringBuffer();
        for (int i = 0; i < linesTree.length; i++) {
            stringBufferTree.append(linesTree[i]);
            stringBufferTree.append(System.getProperty("line.separator"));
        }
        String sqlinsert = "update rule set name=?, description=?, enable=?, type=?, content=?, tree=? where id=?";
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlinsert);
                ps.setString(1, rule.name);
                ps.setString(2, rule.description);
                ps.setString(3, convertToSingleChar(rule.enable));
                ps.setString(4, rule.type);
                byte[] blobContent = stringBufferContent.toString().getBytes();
                ps.setBinaryStream(5, new ByteArrayInputStream(blobContent));
                byte[] blobTree = stringBufferTree.toString().getBytes();
                ps.setBinaryStream(6, new ByteArrayInputStream(blobTree));
                ps.setLong(7, rule.id);
                return ps;
            }
        });
    }

    @Override
    public int enableDisableRule(Rule rule) {
        return jdbcTemplate.update(
                "update rule set enable=? where id=?",
                convertToSingleChar(rule.enable), rule.id);
    }

    @Override
    public Rules getAllRules() {
        int count = jdbcTemplate.queryForObject("select count(*) from rule", Integer.class);
        return jdbcTemplate.query("select * from rule",
                new ResultSetExtractor<Rules>() {
                    List<Rule> list_rules = new ArrayList<>();

                    @Override
                    public Rules extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                        while (resultSet.next()) {
                            Rule rule = new Rule();
                            rule.id = resultSet.getLong("id");
                            rule.name = resultSet.getString("name");
                            rule.description = resultSet.getString("description");
                            rule.type = resultSet.getString("type");
                            rule.enable = resultSet.getString("enable");
                            //rule.content = resultSet.getString("content");
                            try {
                                Blob blobContent = resultSet.getBlob("content");
                                StringBuffer tmpContent = new StringBuffer();
                                String auxContent;
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(blobContent.getBinaryStream()));
                                while ((auxContent = bufferedReader.readLine()) != null) {
                                    tmpContent.append(auxContent).append(System.getProperty("line.separator"));
                                }
                                rule.content = tmpContent.toString();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //rule.tree = resultSet.getString("tree");
                            try {
                                Blob blobTree = resultSet.getBlob("tree");
                                StringBuffer tmpTree = new StringBuffer();
                                String auxTree;
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(blobTree.getBinaryStream()));
                                while ((auxTree = bufferedReader.readLine()) != null) {
                                    tmpTree.append(auxTree).append(System.getProperty("line.separator"));
                                }
                                rule.tree = tmpTree.toString();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            rule.creationdate = ToolsDate.inferFormatAndConvertToNewFormat(resultSet.getString("creationdate"), "dd-MM-yyyy HH:mm:ss");
                            rule.modificationdate = ToolsDate.inferFormatAndConvertToNewFormat(resultSet.getString("modificationdate"), "dd-MM-yyyy HH:mm:ss");
                            list_rules.add(rule);
                        }
                        Rules rules = new Rules();
                        rules.rulesCount = count;
                        rules.list_rules = list_rules;
                        return rules;
                    }
                });
    }

    @Override
    public Features getAllFeatures() {
        return jdbcTemplate.query("select * from feature",
                new ResultSetExtractor<Features>() {
                    List<FeatureBackend> list_features = new ArrayList<>();

                    @Override
                    public Features extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                        while (resultSet.next()) {
                            FeatureBackend featureBackend = new FeatureBackend();
                            featureBackend.id = resultSet.getLong("id");
                            featureBackend.name = resultSet.getString("name");
                            featureBackend.description = resultSet.getString("description");
                            featureBackend.classname = resultSet.getString("classname");
                            featureBackend.transfertype = resultSet.getString("transfertype");
                            featureBackend.enable = resultSet.getString("enable");
                            featureBackend.fixed = resultSet.getString("fixed");
                            featureBackend.value = String.valueOf(resultSet.getDouble("value"));
                            featureBackend.computationtype = resultSet.getString("computationtype");
                            featureBackend.featuretype = resultSet.getString("featuretype");
                            featureBackend.creationdate = ToolsDate.inferFormatAndConvertToNewFormat(resultSet.getString("creationdate"), "dd-MM-yyyy HH:mm:ss");
                            featureBackend.modificationdate = ToolsDate.inferFormatAndConvertToNewFormat(resultSet.getString("modificationdate"), "dd-MM-yyyy HH:mm:ss");
                            featureBackend.rule = resultSet.getString("rule");

                            list_features.add(featureBackend);
                        }
                        Features features = new Features();
                        features.featuresCount = list_features.size();
                        features.listFeatures = list_features;
                        return features;
                    }
                });
    }

    @Override
    public FeatureBackend getFeatureById(Integer featureid) {
        return jdbcTemplate.queryForObject("select * from feature where id = ?", new Object[]{featureid}, new RowMapper<FeatureBackend>() {
            @Override
            public FeatureBackend mapRow(ResultSet resultSet, int i) throws SQLException {
                FeatureBackend featureBackend = new FeatureBackend();
                featureBackend.id = resultSet.getLong("id");
                featureBackend.name = resultSet.getString("name");
                featureBackend.description = resultSet.getString("description");
                featureBackend.classname = resultSet.getString("classname");
                featureBackend.transfertype = resultSet.getString("transfertype");
                featureBackend.enable = String.valueOf(resultSet.getBoolean("enable"));
                featureBackend.fixed = String.valueOf(resultSet.getBoolean("fixed"));
                featureBackend.value = String.valueOf(resultSet.getDouble("value"));
                featureBackend.computationtype = resultSet.getString("computationtype");
                featureBackend.featuretype = resultSet.getString("featuretype");
                featureBackend.creationdate = resultSet.getString("creationdate");
                featureBackend.modificationdate = resultSet.getString("modificationdate");
                featureBackend.rule = resultSet.getString("rule");

                return featureBackend;
            }
        });
    }

    @Override
    public int addNewFeature(FeatureBackend featureBackend) {
        Double value = 0.0;
        if (featureBackend.value != null && !"".equals(featureBackend.value))
            value = Double.valueOf(String.valueOf(featureBackend.value));
        return jdbcTemplate.update(
                "insert into feature (name, description, classname, transfertype, enable, fixed, value, computationtype, featuretype, rule) " +
                        "values(?,?,?,?,?,?,?,?,?,?)",
                featureBackend.name, featureBackend.description, featureBackend.classname, featureBackend.transfertype,
                convertToSingleChar(featureBackend.enable), convertToSingleChar(featureBackend.fixed), value, featureBackend.computationtype, featureBackend.featuretype,
                featureBackend.rule);

    }

    @Override
    public int modifyFeature(FeatureBackend featureBackend) {
        Double value = 0.0;
        if (featureBackend.value != null && !"".equals(featureBackend.value))
            value = Double.valueOf(String.valueOf(featureBackend.value));

        return jdbcTemplate.update(
                "update feature set name=?, description=?, classname=?, transfertype=?, enable=?, fixed=?, value=?, " +
                        "computationtype=?, featuretype=?, rule=? where id=?",
                featureBackend.name, featureBackend.description, featureBackend.classname, featureBackend.transfertype,
                convertToSingleChar(featureBackend.enable), convertToSingleChar(featureBackend.fixed), value, featureBackend.computationtype,
                featureBackend.featuretype, featureBackend.rule, featureBackend.id);
    }

    @Override
    public Integer enableAllFeatures() {
        return jdbcTemplate.update("update feature set enable='Y'");
    }

    @Override
    public List<TransactionResponseTime> getTransactionResponseTime() {
        //select temps as x, avg(duration) as y1 from toto group by temps
        String sql0 = "select temps as x, avg(duration) as y1 from toto group by temps";
        String sql1 = "select settlementdate as x, avg(duration)as y1 from transaction where validated='Y' group by settlementdate";
        return jdbcTemplate.query(sql1,
                new ResultSetExtractor<List<TransactionResponseTime>>() {
                    List<TransactionResponseTime> list_transfer = new ArrayList<>();

                    @Override
                    public List<TransactionResponseTime> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        while (resultSet.next()) {
                            TransactionResponseTime transactionResponseTime = new TransactionResponseTime();

                            LocalDateTime localDateTime = LocalDateTime.parse(resultSet.getString("x"),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                            transactionResponseTime.x = localDateTime
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant().toEpochMilli();
                            transactionResponseTime.y1 = resultSet.getLong("y1");
                            list_transfer.add(transactionResponseTime);
                        }
                        return list_transfer;
                    }
                });
    }

    @Override
    public Scorings getAllScoring() {

        String sql = "select * from scoring";
        return jdbcTemplate.query(sql, new ResultSetExtractor<Scorings>() {
            List<ScoringBackend> list_scoring = new ArrayList<>();

            @Override
            public Scorings extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                while (resultSet.next()) {
                    ScoringBackend scoringBackend = new ScoringBackend();
                    scoringBackend.id = resultSet.getLong("id");
                    scoringBackend.name = resultSet.getString("name");
                    scoringBackend.description = resultSet.getString("description");
                    scoringBackend.enable = resultSet.getString("enable");
                    try {
                        Blob blobContent = resultSet.getBlob("content");
                        StringBuffer tmpContent = new StringBuffer();
                        String auxContent;
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(blobContent.getBinaryStream()));
                        while ((auxContent = bufferedReader.readLine()) != null) {
                            tmpContent.append(auxContent).append(System.getProperty("line.separator"));
                        }
                        scoringBackend.content = tmpContent.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scoringBackend.creationdate = ToolsDate.inferFormatAndConvertToNewFormat(resultSet.getString("creationdate"), "dd-MM-yyyy HH:mm:ss");
                    scoringBackend.modificationdate = ToolsDate.inferFormatAndConvertToNewFormat(resultSet.getString("modificationdate"), "dd-MM-yyyy HH:mm:ss");
                    list_scoring.add(scoringBackend);
                }
                Scorings scorings = new Scorings();
                scorings.scoringcount = list_scoring.size();
                scorings.list_scoring = list_scoring;
                return scorings;
            }
        });
    }

    @Override
    public ScoringBackend getScoringById(Integer scoringid) {
        String sql = "select * from scoring where id = " + scoringid;
        return jdbcTemplate.query(sql, new ResultSetExtractor<ScoringBackend>() {
            @Override
            public ScoringBackend extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                while (resultSet.next()) {
                    try {
                        Blob blobContent = resultSet.getBlob("content");
                        StringBuffer tmpContent = new StringBuffer();
                        String auxContent;
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(blobContent.getBinaryStream()));
                        while ((auxContent = bufferedReader.readLine()) != null) {
                            tmpContent.append(auxContent).append(System.getProperty("line.separator"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }

    @Override
    public int addNewScoring(ScoringBackend scoringBackend) {
        String[] lines = scoringBackend.content.split(System.getProperty("line.separator"));
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < lines.length; i++) {
            stringBuffer.append(lines[i]);
            stringBuffer.append(System.getProperty("line.separator"));
        }
        String sqlinsert = "insert into scoring(name, description, enable, content) values(?,?,?,?)";
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlinsert);
                ps.setString(1, scoringBackend.name);
                ps.setString(2, scoringBackend.description);
                ps.setString(3, scoringBackend.enable);
                byte[] blob = stringBuffer.toString().getBytes();
                ps.setBinaryStream(4, new ByteArrayInputStream(blob));
                return ps;
            }
        });
    }

    @Override
    public int modifyScoring(ScoringBackend scoringBackend) {
        String[] lines = scoringBackend.content.split(System.getProperty("line.separator"));
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < lines.length; i++) {
            stringBuffer.append(lines[i]);
            stringBuffer.append(System.getProperty("line.separator"));
        }
        String sqlupdate = "update scoring set name=?, description=?, enable=?, content=? where id=?";
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlupdate);
                ps.setString(1, scoringBackend.name);
                ps.setString(2, scoringBackend.description);
                ps.setString(3, scoringBackend.enable);
                byte[] blob = stringBuffer.toString().getBytes();
                ps.setBinaryStream(4, new ByteArrayInputStream(blob));
                ps.setLong(5, scoringBackend.id);
                return ps;
            }
        });
    }

    @Override
    public int enableDisableScoring(ScoringBackend scoringBackend) {
        return jdbcTemplate.update(
                "update scoring set enable=? where id=?",
                convertToSingleChar(scoringBackend.enable), scoringBackend.id);
    }

    @Override
    public Integer enableAllScorings() {
        return jdbcTemplate.update("update scoring set enable='Y'");
    }

}
