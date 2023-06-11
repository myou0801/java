#!/bin/bash
pip install awscli-local

cd /docker-entrypoint-initaws.d
awslocal s3 mb s3://sample-bucket
awslocal s3 mv ./sample.log s3://sample-bucket
