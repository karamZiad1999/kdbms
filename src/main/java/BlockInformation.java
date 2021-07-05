public class BlockInformation {

    private int byteOffset;
    private int blockSize;

    public BlockInformation(int byteOffset, int blockSize){
        this.byteOffset = byteOffset;
        this.blockSize = blockSize;

    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getByteOffset(){
        return byteOffset;
    }
}
