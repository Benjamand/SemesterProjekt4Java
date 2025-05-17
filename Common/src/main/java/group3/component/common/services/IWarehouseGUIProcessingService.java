package group3.component.common.services;

import group3.component.common.API.IInstructionAPIProcessingService;
import group3.component.common.API.IWarehouseAPIProcessingService;
import group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;

public interface IWarehouseGUIProcessingService {
     void initializeServices(IWarehouseAPIProcessingService apiProcessingService);
}
