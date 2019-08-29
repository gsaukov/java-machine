package com.apps.cloud.justitia.rest.controller.approval;

import com.apps.cloud.common.rest.ErrorResponse;
import com.apps.cloud.common.rest.RestResponse;
import com.apps.cloud.common.rest.VoidResponse;
import com.apps.cloud.justitia.rest.controller.approval.request.RestRevokeApprovalRequest;
import com.apps.cloud.justitia.rest.controller.approval.response.RestListApprovalDetails;
import com.apps.cloud.justitia.rest.controller.approval.response.RestListApprovalsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/justitia-api/approval")
public class ApprovalController {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/revoke")
    public ResponseEntity<RestResponse> remove(@RequestBody RestRevokeApprovalRequest restRevokeApprovalRequest) {

        int result = jdbcTemplate.update(
                "delete from oauth_approvals where clientId = ? and userId = ? and scope = ?",
                restRevokeApprovalRequest.getClientId(),
                restRevokeApprovalRequest.getUserId(),
                restRevokeApprovalRequest.getScope());

        if (result > 0) {
            return VoidResponse.ok();
        }

        return ErrorResponse.badRequest("APPROVAL_NOT_FOUND");
    }

    @GetMapping("/list")
    public ResponseEntity<RestResponse> list() {
        List<RestListApprovalDetails> approvals = jdbcTemplate.query(
                "select userId, clientId, scope, expiresAt from oauth_approvals",
                (result, number) -> new RestListApprovalDetails.Builder()
                        .withUserId(result.getString("userId"))
                        .withClientId(result.getString("clientId"))
                        .withScope(result.getString("scope"))
                        .withExpiration(FORMATTER.format(result.getDate("expiresAt")))
                        .build());

        return new ResponseEntity<>(new RestListApprovalsResponse.Builder().withApprovals(approvals).build(),
                HttpStatus.OK);
    }

}
