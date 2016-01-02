import java.io.*;
import java.util.*;
import static java.lang.Math.max;

public class HeyAngel
{
    private static Reader in;
    private static PrintWriter out;
    private static int [] arr;

    public static void main (String [] args) throws IOException {
        in = new Reader ();
        out = new PrintWriter (System.out, true);
        int n;
        arr = new int [n + 1];
        
        IntervalTree root = new IntervalTree (1, n);
        
        /*
         * 
         * 
         */
    }
    
    static class IntervalTree {
        public IntervalTree Lchild = null,
        	            Rchild = null;
        public int start, end;	//and few more attributes to be added....................
        
        public IntervalTree () {}
        
        public IntervalTree (int _start, int _end) {
            start = _start; end = _end;
            if (start != end) {
                int mid = (start + end) >> 1;
                Lchild = new IntervalTree (start, mid);
                Rchild = new IntervalTree (mid + 1, end);
                join (this, Lchild, Rchild);
            }
            else {
            	// leaf assigned here
            }
        }
        
        public IntervalTree query (int a, int b) {
            if (a == start && end == b) return this;
            int mid = (start + end) >> 1;
            if (a > mid) return Rchild.query (a, b);
            if (b <= mid) return Lchild.query (a, b);
            IntervalTree ans = new IntervalTree ();
            //join (ans, Lchild.query (a, mid), Rchild.query (mid + 1, b));
            return ans;
        }
        
        public void update(int i, int value) {
            if(i == start && i == end) {
              /*
               *  value to be updated at leaf
               *  arr[i] = value;
               */
              return;
            }
            if(start > i || i > end) return;
            if(Lchild == null) return;
            int mid = (start + end) >> 1;
            if (i > mid) Rchild.update(i, value);
            else         Lchild.update(i, value);
            join (this, Lchild, Rchild);               
        }
        
        public void rangeUpdate(int _start, int _end, int value) {
        	if(_start > end || start > _end) return;
        	if(start == end) {
        	    /*
        	     * value to be updated at leaf
        	     *  arr[i] = value;
        	     */
        	     return;
        	}
            
            //if(Lchild == null) return;
            int mid = (start + end) >> 1;
            if (_start > mid)       Rchild.rangeUpdate(_start, _end, value);
            else if(mid >= _end)    Lchild.rangeUpdate(_start, _end, value);
            else{                   Lchild.rangeUpdate(_start, mid, value);
                                    Rchild.rangeUpdate(mid+1, _end, value);
            }
            join (this, Lchild, Rchild);               
        }
        
        /* Most crucial as well as optional merge function :p
         * public void join (IntervalTree parent, IntervalTree Lchild, IntervalTree Rchild) {
        }*/
    }
}

/** Faster input **/
class Reader {
    final private int BUFFER_SIZE = 1 << 16;
    private DataInputStream din;
    private byte[] buffer;
    private int bufferPointer, bytesRead;
    public Reader(){
        din=new DataInputStream(System.in);
        buffer=new byte[BUFFER_SIZE];
        bufferPointer=bytesRead=0;
    }

    public Reader(String file_name) throws IOException{
        din=new DataInputStream(new FileInputStream(file_name));
        buffer=new byte[BUFFER_SIZE];
        bufferPointer=bytesRead=0;
    }

    public String readLine() throws IOException{
        byte[] buf=new byte[64]; // line length
        int cnt=0,c;
        while((c=read())!=-1){
            if(c=='\n')break;
            buf[cnt++]=(byte)c;
        }
        return new String(buf,0,cnt);
    }

    public int nextInt() throws IOException{
        int ret=0;byte c=read();
        while(c<=' ')c=read();
        boolean neg=(c=='-');
        if(neg)c=read();
        do{ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');
        if(neg)return -ret;
        return ret;
    } 

    public long nextLong() throws IOException{
        long ret=0;byte c=read();
        while(c<=' ')c=read();
        boolean neg=(c=='-');
        if(neg)c=read();
        do{ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');
        if(neg)return -ret;
        return ret;
    }

    public double nextDouble() throws IOException{
        double ret=0,div=1;byte c=read();
        while(c<=' ')c=read();
        boolean neg=(c=='-');
        if(neg)c = read();
        do {ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');
        if(c=='.')while((c=read())>='0'&&c<='9')
            ret+=(c-'0')/(div*=10);
        if(neg)return -ret;
        return ret;
    }
    
    public char nextChar() throws IOException{
        byte c=read();
        while(c<=' ')c=read();
        return (char)c;
    }
    
    private void fillBuffer() throws IOException{
        bytesRead=din.read(buffer,bufferPointer=0,BUFFER_SIZE);
        if(bytesRead==-1)buffer[0]=-1;
    }
    
    private byte read() throws IOException{
        if(bufferPointer==bytesRead)fillBuffer();
        return buffer[bufferPointer++];
    }
    
    public void close() throws IOException{
        if(din==null) return;
        din.close();
    }
}
