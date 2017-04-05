package ua.com.nov.model.dao.fetch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FetchParametersSource<C> {
    private C containerId;
    protected Map<String, Object> parameters;

    public FetchParametersSource(C containerId) {
        this.containerId = containerId;
    }

    public C getContainerId() {
        return containerId;
    }

    public List<Object> getFetchParameters() {
        return new ArrayList<>(parameters.values());
    }

    public Object getParameter(String name) {
       return parameters.get(name);
    }

    public Object addParameter(String name, Object value) {
        return parameters.put(name, value);
    }
}
