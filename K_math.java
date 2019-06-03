import java.util.Random;


class K_math {


    static Random random = new Random(System.currentTimeMillis());

    static double mean = 0;
    static double dev = 1;


    // constructor
    
    K_math() { } static K_math instance = new K_math();


    // matrix initializers
    
    static Double[][] zeros(int hm_rows, int hm_cols) {
      
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row;
        
        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = 0.0;
        }

        return out;
        
    }
    
    static Double[][] ones(int hm_rows, int hm_cols) {
      
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row;
        
        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = 1.0;
        }
        
        return out;
        
    }
    
    static Double[][] randn(int hm_rows, int hm_cols) {
      
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row;
        
        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = random.nextGaussian() * dev + mean;
        }
        
        return out;
        
    }

    static Double[][] identity(int hm_rows, int hm_cols) {

        assert hm_rows == hm_cols;

        Double[][] out = new Double[hm_rows][hm_cols];

        for (int i = 0; i < hm_rows; i++)
            out[i][i] = 1.0;

        return out;

    }

    static Double[][] constants(int hm_rows, int hm_cols, double value) {

        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = value;
        }

        return out;

    }


    // matrix operations

    static Double[][] add(Double[][] a, Double[][] b) {
      
        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a, row_b;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            row_b = b[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = row_a[j] + row_b[j];
        }
        
        return out;
        
    }

    static Double[][] sub(Double[][] a, Double[][] b) {
      
        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a, row_b;
              
        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            row_b = b[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = row_a[j] - row_b[j];
        }

        return out;
        
    }
    
    static Double[][] mul(Double[][] a, Double[][] b) {
      
        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a, row_b;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            row_b = b[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = row_a[j] * row_b[j];
        }

        return out;
        
    }
    
    static Double[][] div(Double[][] a, Double[][] b) {
      
        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a, row_b;
      
        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            row_b = b[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = row_a[j] / row_b[j];
        }
        
        return out;
        
    }
    
    static Double[][] matmul(Double[][] a, Double[][] b) {
      
        int hm_rows1 = a.length;
        int hm_cols1 = a[0].length;
        int hm_rows2 = b.length;
        int hm_cols2 = b[0].length;
        Double[][] out = new Double[hm_rows1][hm_cols2];

        assert hm_cols1 == hm_rows2;

        Double[] row, row_a;
        
        for (int i = 0; i < hm_rows1; i++) {
            row = out[i];
            row_a = a[i];
            for (int j = 0; j < hm_cols2; j++) {
                row[j] = 0.0;
                for (int k = 0; k < hm_cols1; k++)
                    row[j] += row_a[k] * b[k][j];
            }
        }
                
        return out;
        
    }


    // scalar operations
    
    static Double[][] mul_scalar(double b, Double[][] a) {
      
        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;
      
        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = row_a[j] * b;
        }
                
        return out;
        
    } static Double[][] mul_scalar(Double[][] a, double b) { return mul_scalar(b, a); }

    static Double[][] div_scalar(double b, Double[][] a) {

        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = b / row_a[j];
        }

        return out;

    } static Double[][] div_scalar(Double[][] a, double b) { return mul_scalar(1/b, a); }
    
    static Double[][] sub_scalar(double b, Double[][] a) {
      
        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;
                
        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = b - row_a[j];
        }

        return out;
        
    }
    
    static Double[][] sub_scalar(Double[][] a, double b) {
      
        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = row_a[j] - b;
        }
        
        return out;
        
    }
    
    static Double[][] add_scalar(double b, Double[][] a) {
      
        int hm_rows = a.length;
        int hm_cols = a[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = a[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = row_a[j] + b;
        }

        return out;
        
    } static Double[][] add_scalar(Double[][] a, double b) { return add_scalar(b, a); }


    // helpers

    static int size(Double[][] matrix, int dim) {

        if (dim == 0) return matrix.length;
        else          return matrix[0].length;

    } static int[] size(Double[][] matrix) { return new int[]{matrix.length,matrix[0].length}; }
    
    static Double[][] resize(Double[][] matrix, int[] sizes) {

         return vector2matrix(matrix2vector(matrix), sizes);
       
    }

    static Double[][] resize(Double[][] matrix, int size1, int size2) {

        return resize(matrix, new int[]{size1, size2});

    }
    
    static Double[] matrix2vector(Double[][] matrix) {
      
        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[] out = new Double[matrix.length * matrix[0].length];

        int ctr = -1;
        for (Double[] row : matrix) {
            for (int j = 0; j < hm_cols; j++) {

                ctr++;
                out[ctr] = row[j];

            }
        }
        
         return out;
      
    }
    
    static Double[][] vector2matrix(Double[] vector, int[] sizes) {

        Double[][] out = new Double[sizes[0]][sizes[1]];

        int ctr = -1;
        for (Double[] row : out) {
            for (int j = 0; j < sizes[1]; j++) {

                ctr++;
                row[j] = vector[ctr];

            }
        }
        
         return out;
      
    }
    
    static Double[][] transpose(Double[][] matrix) {

        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = new Double[hm_cols][hm_rows];

        Double[] row;

        for (int i = 0; i < matrix.length; i++) {
            row = matrix[i];
            for (int j = 0; j < matrix[0].length; j++)
                out[j][i] = row[j];
        }

        return out;

    }

    static double sum(Double[][] matrix) {

        double sum = 0;

        for (Double[] row : matrix)
            for (Double col : row)
                sum += col;

        return sum;

    }

    static Double[][] sum(Double[][] matrix, int dim) {

        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = null;

        if (dim == 0) {

            out = new Double[1][hm_cols];
            Double[] row = out[0];
            double col_sum;

            for (int j = 0; j < hm_cols; j++) {
                col_sum = 0;

                for (int i = 0; i < hm_rows; i++)
                    col_sum += matrix[i][j];
                row[j] = col_sum;

            }

        }

        if (dim == 1) {

            out = new Double[hm_rows][1];
            double row_sum;

            int i = -1;
            for (Double[] row : matrix) {
                i++;
                row_sum = 0;

                for (Double e : row)
                    row_sum += e;
                out[i][0] = row_sum;

            }

        }

        return out;

    }


    // vector operations

    static Double[] vector_mul(Double[] v1, Double[] v2) {

        assert v1.length == v2.length;

        int hm_elements = v1.length;
        Double[] out = new Double[hm_elements];

        for (int i = 0; i < hm_elements; i++)
            out[i] = v1[i] * v2[i];

        return out;

    }

    static Double[] vector_add(Double[] v1, Double[] v2) {

        assert v1.length == v2.length;

        int hm_elements = v1.length;
        Double[] out = new Double[hm_elements];

        for (int i = 0; i < hm_elements; i++)
            out[i] = v1[i] + v2[i];

        return out;

    }

    static double vector_sum(Double[] v) {

        double doubl = 0;

        for (Double d : v) doubl += d;

        return doubl;

    }


    // special operations

    static Double[][] pow(Double[][] matrix, double power) {
      
        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = matrix[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = Math.pow(row_a[j], power);
        }
                
        return out;
        
    }

    static Double[][] exp(Double[][] matrix) {

        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = matrix[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = Math.exp(row_a[j]);
        }

        return out;

    }

    static Double[][] log(Double[][] matrix) {

        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = matrix[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = Math.log(row_a[j]);
        }

        return out;

    }

    static Double[][] sigm(Double[][] matrix) {
      
        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = matrix[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = (1.0 / (1 + Math.exp(-row_a[j])));
        }

        return out;
       
    }
    
    static Double[][] tanh(Double[][] matrix) {
      
        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = matrix[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = Math.tanh(row_a[j]);
        }

        return out;
       
    }

    static Double[][] cross_entropy(Double[][] target, Double[][] output) {

        int hm_rows = target.length;
        int hm_cols = target[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a, row_b;

        for (int i = 0; i < hm_rows; i++) {
            row = out[i];
            row_a = target[i];
            row_b = output[i];
            for (int j = 0; j < hm_cols; j++)
                row[j] = -(row_a[j]*Math.log(row_b[j]));
        }

        return out;
   
    }
    
    static Double[][] softmax(Double[][] matrix) {
        
        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        Double[] row, row_a;

        double sum = 0;

        for (int i = 0; i < hm_rows; i++) {
            row_a = matrix[i];
            for (int j = 0; j < hm_cols; j++)
                sum += Math.exp(row_a[j]);
        }
      
        if (hm_rows == 1) {

            row = out[0];
            row_a = matrix[0];

            for (int k = 0; k < hm_cols; k++)
                row[k] = Math.exp(row_a[k]) / sum;
        }
        
        if (hm_cols == 1)

            for (int k = 0; k < hm_rows; k++)
                out[k][0] = Math.exp(matrix[k][0])/sum;
        
        return out;
        
    }
    
    static Double[][] softmax(Double[][] matrix, int index_begin, int index_end) {
        
        int hm_rows = matrix.length;
        int hm_cols = matrix[0].length;
        Double[][] out = new Double[hm_rows][hm_cols];

        double sum = 0;
      
        if (hm_rows == 1) {

            Double[] row = out[0];
            Double[] row_a = matrix[0];

            for (int k = index_begin; k < index_end; k++)
                sum += Math.exp(row_a[k]);
            for (int k = index_begin; k < index_end; k++)
                row[k] = Math.exp(row_a[k])/sum;
           
        }
            
        if (hm_cols == 1) {
          
            for (int k = index_begin; k < index_end; k++)
                sum += Math.exp(matrix[k][0]);
            for (int k = index_begin; k < index_end; k++)
                out[k][0] = Math.exp(matrix[k][0])/sum;
          
        }
            
        return out;
        
    }

}