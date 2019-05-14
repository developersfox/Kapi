import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


// 2DO: compile as .jar

class K_neural {


    K_base base = new K_base();

    int hm_cores = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(hm_cpu);

    static Object make_model(String model_type, int in_size, ArrayList<Integer> hidden_sizes, int out_size) {

        if (model_type.equals("gru")) return new GRU(int in_size, ArrayList<Integer> hidden_sizes, int out_size);
        if (model_type.equals("lstm")) return new LSTM(int in_size, ArrayList<Integer> hidden_sizes, int out_size);

    }

    static LOAD(String source) {

        // todo : do.

    }

    static SAVE(String destination) {

        // todo : do.

    }

    private class R implements Callable<GRU, ArrayList<Double[][]>>{

        GRU model;
        ArrayList<Double[][]> sequence;

        R(model,  sequence) {

            this.model = model;
            this.sequence = sequence;

        }

        ArrayList<Double[][]> call() {

            return model.respond_to(sequence);

        }

    }

    static ArrayList<ArrayList<Double[][]>> batch_response(GRU model, ArrayList<ArrayList<Double[][]>> batch) {

        int batch_size = batch.size();

        Future<ArrayList<Double[][]>>[] promises = new Future[batch_size];

        ctr = -1;
        for (ArrayList<Double[][]> data : batch) {
            ctr++;

            // runs[ctr] = new R(model, data);
            // pool.execute(runs[ctr]);
            // pool.execute(new R(model, data));
            promises[ctr] = pool.submit(new R(model, data));

        }

        ArrayList<Double[][]>[] responses = new ArrayList<Double[][]>[batch_size];

        for (int i = 0; i < batch_size; i++)

            responses[i] = promises.get(i).get();

        return responses;

    }
    
}

class GRU {


    private ArrayList<ArrayList<Double[][]>> layers = new ArrayList();
    private ArrayList<Double[][]> states = new ArrayList();


    GRU(int in_size, ArrayList<Integer> hidden_sizes, int out_size) {

        int hm_layers = hidden_sizes.size()+1;

        for (int l = 0; l < hm_layers; l++) {

            ArrayList<Double[][]> layer = new ArrayList();
            int i,o;

            if      (l == 0)           { i = in_size ; o = hidden_sizes.get(0); }
            else if (l == hm_layers-1) { i = hidden_sizes.get(l-1) ; o = hidden_sizes.get(l); }
            else                       { i = hidden_sizes.get(l) ; o = out_size; }

            // @params:
            //"wf1": randn(in_size, layer_size,    requires_grad=True, dtype=float32),
            //"wf2": randn(layer_size, layer_size, requires_grad=True, dtype=float32),
            //"wk1": randn(in_size, layer_size,    requires_grad=True, dtype=float32),
            //"wk2": randn(layer_size, layer_size, requires_grad=True, dtype=float32),
            //"wi" : randn(in_size, layer_size,    requires_grad=True, dtype=float32),

            layer.add(base.randn(i,o));
            layer.add(base.randn(o,o));
            layer.add(base.randn(i,o));
            layer.add(base.randn(o,o));
            layer.add(base.randn(i,o));

            layers.add(layer);
            states.add(base.zeros(o,o));

        }

    }

    private zero_states() {

        int ctr = -1;
        for (ArrayList<Double[][]> layer : layers) {
            ctr++;

            int layer_size = base.size(layer.get(0))[1];
            Double[][] zero_state = base.zeros(1, layer_size);
            states.set(ctr, zero_state);

        }

    }

    private Double[][] propogate_layer(ArrayList<Double[][]> layer, Double[][] in, int layer_ctr) {

        state  = states.get(layer_ctr);
        focus  = base.sigm(base.add(base.matmul(in,layer.get(0)),base.matmul(state,layer.get(1))));
        keep   = base.sigm(base.add(base.matmul(in,layer.get(2)),base.matmul(state,layer.get(3))));
        interm = base.tanh(base.add(base.matmul(inp,layer.get(4)),base.mul(state,focus)));
        new_state = base.add(base.mul(keep,interm),base.mul(base.sub_scalar(1,keep),state));
        states.set(layer_ctr, new_state);
        out = new_state;

        return out;

    }

    private Double[][] propogate_model(Double[][] in) {


        int ctr = -1;
        for (ArrayList<Double[][]> layer : layers) {
            ctr++;

            in = propogate_layer(layer, in, ctr);

        }

        return in;

    }

    ArrayList<Double[][]> respond_to(ArrayList<Double[][]> sequence) {

        ArrayList<Double[][]> response = new ArrayList();

        for (Double[][] timestep : sequence)

            response.add(propogate_model(timestep));

        zero_states();

        return response;

    }

}

class LSTM {


    private ArrayList<ArrayList<Double[][]>> layers = new ArrayList();
    private ArrayList<Double[][]> states = new ArrayList();


    LSTM(int in_size, ArrayList<Integer> hidden_sizes, int out_size) {

        int hm_layers = hidden_sizes.size()+1;

        for (int l = 0; l < hm_layers; l++) {

            ArrayList<Double[][]> layer = new ArrayList();
            int i, o;

            if      (l == 0)           { i = in_size ; o = hidden_sizes.get(0); }
            else if (l == hm_layers-1) { i = hidden_sizes.get(l-1) ; o = hidden_sizes.get(l); }
            else                       { i = hidden_sizes.get(l) ; o = out_size; }

            // @params:
            //"wf1": randn(in_size, layer_size,    requires_grad=True, dtype=float32),
            //"wf2": randn(layer_size, layer_size, requires_grad=True, dtype=float32),
            //"wk1": randn(in_size, layer_size,    requires_grad=True, dtype=float32),
            //"wk2": randn(layer_size, layer_size, requires_grad=True, dtype=float32),
            //"wi1": randn(in_size, layer_size,    requires_grad=True, dtype=float32),
            //"wi2": randn(layer_size, layer_size, requires_grad=True, dtype=float32),
            //"ws1": randn(in_size, layer_size,    requires_grad=True, dtype=float32),
            //"ws2": randn(layer_size, layer_size, requires_grad=True, dtype=float32),

            layer.add(base.randn(i,o));
            layer.add(base.randn(o,o));
            layer.add(base.randn(i,o));
            layer.add(base.randn(o,o));
            layer.add(base.randn(i,o));
            layer.add(base.randn(o,o));
            layer.add(base.randn(i,o));
            layer.add(base.randn(o,o));

            layers.add(layer);

        }

    }

    private zero_states() {

        int ctr = -1;
        for (ArrayList<Double[][]> layer : layers) {
            ctr++;

            int layer_size = base.size(layer.get(0))[1];
            Double[][] zero_state = base.zeros(1, layer_size);
            states.set(ctr, zero_state);

        }

    }

    private Double[][] propogate_layer(ArrayList<Double[][]> layer, Double[][] in, int layer_ctr) {

        state  = states.get(layer_ctr);
        forget = base.sigm(base.add(base.matmul(in,layer.get(0)),base.matmul(state,layer.get(1))));
        keep   = base.sigm(base.add(base.matmul(in,layer.get(2)),base.matmul(state,layer.get(3))));
        interm = base.tanh(base.add(base.matmul(in,layer.get(4)),base.mul(state,layer.get(5))));
        show   = base.sigm(base.add(base.matmul(in,layer.get(6)),base.matmul(state,layer.get(7))));
        new_state = base.add(base.mul(keep,interm),base.mul(forget,state));
        states.set(layer_ctr, new_state);
        out = base.mul(show,base.tanh(new_state));

        return out;

    }

    private Double[][] propogate_model(Double[][] in) {


        int ctr = -1;
        for (ArrayList<Double[][]> layer : layers) {
            ctr++;

            in = propogate_layer(layer, in, ctr);

        }

        return in;

    }

    ArrayList<Double[][]> respond_to(ArrayList<Double[][]> sequence) {

        ArrayList<Double[][]> response = new ArrayList();

        for (Double[][] timestep : sequence)

            response.add(propogate_model(timestep));

        zero_states();

        return response;

    }


}
