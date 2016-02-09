package promitech.was_jpa_trace_logs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class JpaLogs {
    private static Pattern SPENT_PATTERN = Pattern.compile("\\[(.*?) ms\\] spent");
    private static Pattern JPA_QUERY_PATTERN = Pattern.compile("openjpa.Query:(.*?)\\[(.*?)\\]");
    
    private HashMap<String, Integer> sqlCount = new HashMap<String, Integer>();
    private HashMap<String, String> jpaForSql = new HashMap<String, String>();
    private HashMap<String, Integer> sqlMaxTime = new HashMap<String, Integer>();
    private HashMap<String, Integer> sqlMinTime = new HashMap<String, Integer>();
    
    private void increaseSqlCount(String sql) {
        Integer count = sqlCount.get(sql);
        if (count == null) {
            count = 0;
        }
        count++;
        sqlCount.put(sql, count);
    }
    
    private void updateSqlTime(String sql, Integer time) {
        Integer t = sqlMaxTime.get(sql);
        if (t == null) {
            sqlMaxTime.put(sql, time);
        } else {
            if (time > t) {
                sqlMaxTime.put(sql, time);
            }
        }
        
        t = sqlMinTime.get(sql);
        if (t == null) {
            sqlMinTime.put(sql, time);
        } else {
            if (time < t) {
                sqlMinTime.put(sql, time);
            }
        }
    }
    
    public String sqlQueryFromLogLine(String line) {
        if (line.indexOf("openjpa.jdbc.SQL") == -1) {
            return null;
        }
        
        int prepIdx = line.indexOf("prepstmnt");
        if (prepIdx == -1) {
            return null;
        }
        String sql = line.substring(prepIdx + "prepstmnt".length() + 1 );
        int firstSpaceIdx = sql.indexOf(" ");
        sql = sql.substring(firstSpaceIdx + 1);
        
        int paramIdx = sql.indexOf("[params");
        if (paramIdx != -1) {
            sql = sql.substring(0, paramIdx);
        }
        return sql;
    }

    private String jpaQueryFromLogLine(String line) {
        if (line.indexOf("openjpa.Query:") == -1) {
            return null;
        }
        Matcher m = JPA_QUERY_PATTERN.matcher(line);
        if (m.find()) {
            return m.group(2);
        }
        return null;
    }

    private void addJpaQlForSql(String sql, String jpaQl) {
        jpaForSql.put(sql, jpaQl);
    }

    private Integer sqlSpentTime(String line) {
        int sqlIdx = line.indexOf("openjpa.jdbc.SQL");
        if (sqlIdx == -1) {
            return null;
        }
        line = line.substring(sqlIdx);
        Matcher m = SPENT_PATTERN.matcher(line);
        if (m.find()) {
            //System.out.println("spent " + m.group(1));
            return Integer.valueOf(m.group(1));
        }
        return null;
    }
    
    private void load(String filename) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

        String lastJpaQl = null;
        String line = null;
        String lastSql = null;
        
        while ((line = bufferedReader.readLine()) != null) {
            String jpaQl = jpaQueryFromLogLine(line);
            if (jpaQl != null) {
                lastJpaQl = jpaQl;
            }
            String sql = sqlQueryFromLogLine(line);
            
            if (StringUtils.isNotBlank(sql)) {
                increaseSqlCount(sql);
                if (StringUtils.isNotBlank(lastJpaQl)) {
                    addJpaQlForSql(sql, lastJpaQl);
                }
                lastJpaQl = null;
                lastSql = sql;
            } else {
                Integer sqlSpenttime = sqlSpentTime(line);
                if (sqlSpenttime != null) {
                    updateSqlTime(lastSql, sqlSpenttime);
                    //System.out.println(" " + sqlSpenttime + "  " + lastSql);
                }
            }
        }
        bufferedReader.close();
    }
    
    private void print() {
        for (Entry<String, Integer> entrySet : sqlCount.entrySet()) {
            Integer maxSpent = sqlMaxTime.get(entrySet.getKey());
            System.out.println(" " + entrySet.getValue() + " "  + maxSpent + " " + entrySet.getKey());
        }
    }

    private void saveToFile() throws Exception {
        String filename = "" + System.currentTimeMillis() + "_sql_logs.csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write("count;max spent time;min spent time;jpaQuery;query;");
        bw.newLine();

        for (Entry<String, Integer> entrySet : sqlCount.entrySet()) {
            
            String sql = entrySet.getKey();
            Integer count = entrySet.getValue();
            Integer maxSpent = sqlMaxTime.get(sql);
            Integer minSpent = sqlMinTime.get(sql);
            String jpaQl = jpaForSql.get(sql);
            if (jpaQl == null) {
                jpaQl = "";
            }

            StringBuilder l = new StringBuilder()
                .append(count).append(";")
                .append(maxSpent).append(";")
                .append(minSpent).append(";")
                .append(jpaQl).append(";")
                .append(sql).append(";");
            bw.write(l.toString());
            bw.newLine();
        }
        bw.close();
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("start");
        String fn = "c:\\WAS7_64\\profiles\\axaProfile\\logs\\server1\\trace.log";
        
        JpaLogs l = new JpaLogs();
        l.load(fn);
        l.print();
        l.saveToFile();
        System.out.println("koniec");
    }

}
