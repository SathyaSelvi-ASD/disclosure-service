# Work Action Create API

## API

- Method: POST
- Path: `/api/work-actions`

## Request

CreateWorkActionRequest

## Response

### Success Response

- HTTP Status: `200 OK`
- Response Type: `ApiResponse<CreateWorkActionResponse>`

### Response Structure

```text
ApiResponse
├── data
│   └── CreateWorkActionResponse

## Business Requirement

Create a new work action for the customer.

## Validation

- customerId is mandatory
- workActionType is mandatory

## Success Response

HTTP 200

## Error Handling

Use the existing RFC 9457 Problem Details implementation.

## Testing

Generate:

- Controller unit tests
- Service/UseCase unit tests