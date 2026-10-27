# camel-rest-basic

Proyecto basico para utilizacion de Rutas Camel.

Pipeline de Tekton y manifiestos de GitOps en este mismo repositorio. No hay Dockerfile: el pipeline clona `master`, ejecuta Maven y publica el fat jar.

La imagen queda en el registro interno de OpenShift:

`image-registry.openshift-image-registry.svc:5000/camel-rest/rest-gateway:latest`

La aplicación escucha en el puerto 8080 (`/camel-gateway/sayhello/`) y expone health de Spring Boot en el puerto 8081 (`/actuator/health`).

## Requisitos

- OpenShift con el operador OpenShift Pipelines instalado.
- El proyecto Maven usa el BOM de Fuse 7.6 y Java 11. El clúster necesita salida a GitHub, Maven Central, `maven.repository.redhat.com` y `registry.access.redhat.com`.

## Instalar y construir

```bash
oc apply -k .
oc get pipelinerun -n camel-rest
oc logs -n camel-rest -l tekton.dev/pipelineRun=camel-rest-basic -f
```

El `PipelineRun` `camel-rest-basic` arranca con ese apply. Para repetir el build:

```bash
oc delete pipelinerun camel-rest-basic -n camel-rest
oc apply -k .
```

Ruta de la aplicación:

```bash
oc get route rest-gateway -n camel-rest
```

## GitOps con Argo CD

El controlador de OpenShift GitOps no puede crear los RoleBinding de `pipelines-scc` ni de `system:image-builder`. Esos objetos se aplican una vez, como cluster-admin. La Application reconcilia el resto (`components/camel-rest/base`), incluido el `PipelineRun`.

```bash
oc apply -k bootstrap
oc apply -f clusters/default/camel-rest-application.yaml
```

La Application apunta a `https://github.com/abelluque/camel-rest-basic.git`, rama `master`, path `components/camel-rest/base`.

Con `selfHeal: true`, borrar el `PipelineRun` lo vuelve a crear y relanza el build. El objeto es inmutable: si hay que cambiar parámetros, borrarlo y dejar que Argo CD lo recree.

## Parámetros

Los valores por defecto están en `components/camel-rest/base/pipeline.yaml` y la imagen del Deployment en `deployment.yaml`. Hay que mantener las dos referencias alineadas.

| Parámetro | Default |
| --- | --- |
| `git-url` | `https://github.com/abelluque/camel-rest-basic` |
| `git-revision` | `master` |
| `image` | `image-registry.openshift-image-registry.svc:5000/camel-rest/rest-gateway:latest` |
