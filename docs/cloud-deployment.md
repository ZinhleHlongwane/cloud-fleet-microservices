# Cloud deployment design

The repository is cloud-ready but does not embed credentials.

## AWS option
- Application containers: ECS/Fargate or App Runner.
- PostgreSQL: Amazon RDS for PostgreSQL.
- Secrets: AWS Secrets Manager / Parameter Store.
- Metrics: CloudWatch plus Prometheus/Grafana if retained.
- Broker: Amazon MQ for ActiveMQ.

## Google Cloud option
- Application containers: Cloud Run or GKE.
- PostgreSQL: Cloud SQL for PostgreSQL.
- Secrets: Secret Manager.
- Metrics: Cloud Monitoring.
- Broker: self-managed ActiveMQ on GKE/Compute Engine or replace with Pub/Sub through an adapter.

For an internship portfolio, deploy one environment for real and keep the other as an architecture alternative.
