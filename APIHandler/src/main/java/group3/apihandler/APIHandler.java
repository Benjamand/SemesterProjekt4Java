package group3.apihandler;

import group3.component.common.API.Warehouse;

import java.io.IOException;

public interface APIHandler {
    public Warehouse getWarehouseInfo() throws IOException;
}
