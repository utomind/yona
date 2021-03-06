/**
 * Yobi, Project Hosting SW
 *
 * Copyright 2015 NAVER Corp.
 * http://yobi.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package data.exchangers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import data.DefaultExchanger;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Yi EungJun
 */
public class NotificationEventDataExchanger extends DefaultExchanger {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String SENDER_ID = "sender_id";
    private static final String CREATED = "created";
    private static final String RESOURCE_TYPE = "resource_type";
    private static final String RESOURCE_ID = "resource_id";
    private static final String EVENT_TYPE = "event_type";
    private static final String OLD_VALUE = "old_value";
    private static final String NEW_VALUE = "new_value";

    @Override
    protected void setPreparedStatement(PreparedStatement ps, JsonNode node) throws SQLException {
        short index = 1;
        ps.setLong(index++, node.get(ID).longValue());
        ps.setString(index++, node.get(TITLE).textValue());
        setNullableLong(ps, index++, node, SENDER_ID);
        ps.setTimestamp(index++, timestamp(node.get(CREATED).longValue()));
        ps.setString(index++, node.get(RESOURCE_TYPE).textValue());
        ps.setString(index++, node.get(RESOURCE_ID).textValue());
        ps.setString(index++, node.get(EVENT_TYPE).textValue());
        setClob(ps, index++, node, OLD_VALUE);
        setClob(ps, index++, node, NEW_VALUE);
    }

    @Override
    protected void setNode(JsonGenerator generator, ResultSet rs) throws IOException, SQLException {
        short index = 1;
        putLong(generator, ID, rs, index++);
        putString(generator, TITLE, rs, index++);
        putLong(generator, SENDER_ID, rs, index++);
        putTimestamp(generator, CREATED, rs, index++);
        putString(generator, RESOURCE_TYPE, rs, index++);
        putString(generator, RESOURCE_ID, rs, index++);
        putString(generator, EVENT_TYPE, rs, index++);
        putClob(generator, OLD_VALUE, rs, index++);
        putClob(generator, NEW_VALUE, rs, index++);
    }

    @Override
    public String getTable() {
        return "NOTIFICATION_EVENT";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO NOTIFICATION_EVENT " +
                "(ID, TITLE, SENDER_ID, CREATED, RESOURCE_TYPE, " +
                "RESOURCE_ID, EVENT_TYPE, OLD_VALUE, NEW_VALUE) " + values(9);
    }

    @Override
    protected String getSelectSql() {
        return "SELECT ID, TITLE, SENDER_ID, CREATED, RESOURCE_TYPE, " +
                "RESOURCE_ID, EVENT_TYPE, OLD_VALUE, NEW_VALUE " +
                "FROM NOTIFICATION_EVENT";
    }
}
