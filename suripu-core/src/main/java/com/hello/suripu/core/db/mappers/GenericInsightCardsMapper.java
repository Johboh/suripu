package com.hello.suripu.core.db.mappers;

import com.hello.suripu.core.models.Insights.GenericInsightCards;
import com.hello.suripu.core.models.Insights.InsightCard;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kingshy on 1/15/15.
 */
public class GenericInsightCardsMapper implements ResultSetMapper<GenericInsightCards> {
    @Override
    public GenericInsightCards map(int index, ResultSet r, StatementContext ctx) throws SQLException {

        return new GenericInsightCards(r.getInt("id"),
                InsightCard.Category.fromInteger(r.getInt("category")),
                r.getString("text"),
                r.getString("image_url"));
    }

}
