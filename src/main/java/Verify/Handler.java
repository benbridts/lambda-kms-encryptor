package Verify;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.nio.ByteBuffer;
import java.util.Base64;

public class Handler implements RequestHandler<Request, Response> {
    public Response handleRequest(Request request, Context context) {
        AWSKMSClient client = new AWSKMSClient();
        client.setRegion(Region.getRegion(Regions.EU_WEST_1));

        ByteBuffer ciphertextBlob = ByteBuffer.wrap(Base64.getDecoder().decode(request.getSecret()));
        DecryptRequest decryptRequest = new DecryptRequest().withCiphertextBlob(ciphertextBlob);
        DecryptResult decryptResult = client.decrypt(decryptRequest);

        String plaintext = new String(decryptResult.getPlaintext().array());
        if (request.getPlaintext().equals(plaintext)) {
            return new Response(true);
        }

        return new Response(false);
    }
}
