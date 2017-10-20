package strategies;

import javafx.beans.property.SimpleStringProperty;
import org.apache.openjpa.meta.JavaTypes;
import strategies.AbstractPropertyValueHandler;

public class StringPropertyValueHandler extends AbstractPropertyValueHandler {

    private static final long serialVersionUID = 706389091202934571L;

    @Override
    protected Object mapToObject(Object value) {
        return new SimpleStringProperty(value.toString());
    }

    @Override
    int getDatabaseColumnType() {
        return JavaTypes.STRING;
    }
}