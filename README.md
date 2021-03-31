# Use opentracing with Istio for tracing propagation and method-level tracing inside a service

### How to run this demo?
1. deploy kafka 
2. deploy eshop demo
```
git clone https://github.com/aeraki-framework/method-level-tracing-with-istio.git
kubectl apply -f istio-opentracing-demo/k8s/kafka.yaml
kubectl apply -f istio-opentracing-demo/k8s/eshop.yaml
```
3. Open this url in the browser to trigger the eshop service http://${INGRESS_EXTERNAL_IP}/checkout
4. Open TCM UI in the browser to view the tracing

![](https://raw.githubusercontent.com/zhaohuabing/istio-opentracing-demo/master/screenshot/istio-tracing-opentracing-kafka.jpg)

### Understanding what happened under the hood
* The eshop demo uses opentracing and Zipkin for distributed tracing instrumentation. All the REST calls are automatically traced by opentracing.
* A 'Traced' AOP annotation is used for method-level tracing instrumentation.
* Zipkin opentracing implementation handles the trace context propagation crossing process boundaries. So we don't have to explicitly copy the tracing headers from downstream requests to upstream requests.
* A TracingKafka2RestTemplateInterceptor is used to propagate trace context from kafka headers to HTTP headers.

More at https://zhaohuabing.com/post/2019-06-22-using-opentracing-with-istio/

