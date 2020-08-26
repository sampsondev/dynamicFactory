package practice;




import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretRequest;

import java.util.Base64;

public class PutAndGetSecrets {
    public static void main(String[] args){
        System.out.println("use secrets manager");


        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.EU_CENTRAL_1).build();
//
        String value=";lj;oij;j";
        String mySecretKey="sdfsljf;kje;r";
        CreateSecretRequest request =  CreateSecretRequest.builder().name("friendy_new_secret2")
                .description("practice secrets")
                .secretString("{\"VDI_CLIENT_SECRET\":\"" + value + "\"," +
                        "\"my_aws_secret\":\"" + mySecretKey + "\"}").build();

        client.createSecret(request);

//        DeleteSecretRequest dequest=  DeleteSecretRequest.builder()
//                .secretId("friendy_new_secret2")
//                .forceDeleteWithoutRecovery(true).build();
//        client.deleteSecret(dequest);
//        getSecret();


    }
//
//    public static void getSecret() {
//
//        String secretName = "aws_secret_codes";
//        String region = "us-west-1";
//
//        // Create a Secrets Manager client
//        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
//                .withRegion(region)
//                .build();
//
//        // In this sample we only handle the specific exceptions for the 'GetSecretValue' API.
//        // See https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
//        // We rethrow the exception by default.
//
//        String secret, decodedBinarySecret;
//        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
//                .withSecretId(secretName);
//        GetSecretValueResult getSecretValueResult = null;
//
//        try {
//            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
//        } catch (DecryptionFailureException e) {
//            // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        } catch (InternalServiceErrorException e) {
//            // An error occurred on the server side.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        } catch (InvalidParameterException e) {
//            // You provided an invalid value for a parameter.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        } catch (InvalidRequestException e) {
//            // You provided a parameter value that is not valid for the current state of the resource.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        } catch (ResourceNotFoundException e) {
//            // We can't find the resource that you asked for.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        }
//
//        // Decrypts secret using the associated KMS CMK.
//        // Depending on whether the secret is a string or binary, one of these fields will be populated.
//        if (getSecretValueResult.getSecretString() != null) {
//            secret = getSecretValueResult.getSecretString();
//            System.out.println("secret string " + secret);
//        }
//        else {
//            decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
//        }
//
//        // Your code goes here.
//    }

}
