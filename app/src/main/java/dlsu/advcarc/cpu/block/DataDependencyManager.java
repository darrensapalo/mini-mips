package dlsu.advcarc.cpu.block;

import dlsu.advcarc.parser.Instruction;
import dlsu.advcarc.parser.Parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Darren on 11/22/2015.
 */
public class DataDependencyManager {

    public enum DataHazardType {
        ReadAfterWrite, WriteAfterWrite
    }

    public static final DataDependencyBlock NoBlock = new DataDependencyBlock(null, null, null, null);

    private static LinkedList<DataDependencyBlock> blocks = new LinkedList<>();

    public void addDependencyBlock(DataDependencyBlock block){
        blocks.add(block);
    }

    public void removeDependencyBlock(DataDependencyBlock block){
        blocks.remove(block);
    }

    public DataDependencyBlock getBlock(int i){
        return blocks.get(i);
    }

    public int size(){
        return blocks.size();
    }

    public Iterator<DataDependencyBlock> iterator(){
        return blocks.iterator();
    }

    public DataDependencyBlock getBlockOwnedBy(Instruction instruction) {
        for (DataDependencyBlock block : blocks){
            if (block.getOwnedBy().equals(instruction))
                return block;
        }
        return null;
    }

    public void removeBlockOwnedBy(Instruction instruction) {
        DataDependencyBlock block = getBlockOwnedBy(instruction);
        removeDependencyBlock(block);

        ArrayList<Parameter> parameters = instruction.getParameters();
        parameters.forEach(parameter -> parameter.dequeueDependency());
    }

    public static class DataDependencyBlock {

        private final Instruction.Stage releaseStage;
        private final Instruction.Stage blockStage;
        private final Instruction ownedBy;
        private final DataHazardType dataHazardType;

        public DataDependencyBlock(Instruction ownedBy, Instruction.Stage releaseStage, Instruction.Stage blockStage, DataHazardType dataHazardType) {
            this.releaseStage = releaseStage;
            this.blockStage = blockStage;
            this.ownedBy = ownedBy;
            this.dataHazardType = dataHazardType;
            System.out.println("Generated a new data dependency on " + ownedBy + " to be blocked at " + blockStage + " released at " + releaseStage + ". This data hazard is of type " + dataHazardType + ".");
        }

        public Instruction.Stage getReleaseStage() {
            return releaseStage;
        }

        public Instruction.Stage getBlockStage() {
            return blockStage;
        }

        public Instruction getOwnedBy() {
            return ownedBy;
        }

        public DataHazardType getDataHazardType() {
            return dataHazardType;
        }
    }
}
