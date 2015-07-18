package Encrypt;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.Base64;

import java.nio.ByteBuffer;

public class Handler implements RequestHandler<Request, Response> {
    public Response handleRequest(Request request, Context context) {
        AWSKMSClient client = new AWSKMSClient();
        client.setRegion(Region.getRegion(Regions.EU_WEST_1));

        EncryptRequest encryptRequest = new EncryptRequest()
                .withKeyId(request.getKeyId())
                .withPlaintext(ByteBuffer.wrap(request.getPlaintext().getBytes()));
        EncryptResult encryptResult = client.encrypt(encryptRequest);

        ByteBuffer ciphertextBlob = encryptResult.getCiphertextBlob();
        return new Response(new String(Base64.encode(ciphertextBlob.array())));
    }
}


