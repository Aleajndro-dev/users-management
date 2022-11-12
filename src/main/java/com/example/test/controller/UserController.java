package com.example.test.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.test.dto.ErrorResponseDTO;
import com.example.test.dto.GetUserDTO;
import com.example.test.dto.ImageResponseDTO;
import com.example.test.dto.SaveUserDTO;
import com.example.test.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private UsersService userService;

	public UserController(UsersService userService) {
		super();
		this.userService = userService;
	}
	
	@ResponseBody
	@Operation(summary = "Save a new user.")
	@RequestBody(content = {
			@Content(mediaType = "multipart/form-data", schema = @Schema(implementation = SaveUserDTO.class)) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User saved successfully.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Data not validated.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }),
			@ApiResponse(responseCode = "415", description = "Type image not validated.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Failed to save image.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }) })
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetUserDTO> saveUser( @RequestParam("photo") MultipartFile file,
			@Parameter(hidden = true) @RequestParam("name") String name, 
			@Parameter(hidden = true) @RequestParam("emails") List<String> emails,
			@Parameter(hidden = true) @RequestParam("gender") String gender, 
			@Parameter(hidden = true) @RequestParam("status") Integer status){
		SaveUserDTO user = new SaveUserDTO(name,file,emails,gender,status);

		return new ResponseEntity<GetUserDTO>(userService.createUser(user), HttpStatus.CREATED);
	}
	
	
	@Operation(summary = "Get user list.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Get list.", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = 
					@Schema(implementation = GetUserDTO.class))) }) })
	@GetMapping("/list")
	public List<GetUserDTO> getUserList(@RequestParam(defaultValue = "1") int page){
		return userService.getUserList(page-1);
	}
	
	@Operation(summary = "Get user by id.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Get user."),
			@ApiResponse(responseCode = "404", description = "User not found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }) })
	@GetMapping("/{id}")
	public ResponseEntity<GetUserDTO> getUserById(@PathVariable("id") Long userId){
		return new ResponseEntity<GetUserDTO>(userService.getUserById(userId), HttpStatus.OK);
	}
	
	@Operation(summary = "Update user.")
	@RequestBody(content = {
			@Content(mediaType = "multipart/form-data", schema = @Schema(implementation = SaveUserDTO.class, required = false)) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User updated successfully.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Data not validated.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }),
			@ApiResponse(responseCode = "415", description = "Type image not validated.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Failed to save image.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }) })
	@PutMapping("/edit/{id}")
	public ResponseEntity<GetUserDTO>editUser(@PathVariable("id") Long userId, 
			@Parameter(hidden = true) @RequestParam(value = "photo", required = false) MultipartFile file,
			@Parameter(hidden = true) @RequestParam(value = "name", required = false) String name, 
			@Parameter(hidden = true) @RequestParam(value = "emails", required = false) List<String> emails,
			@Parameter(hidden = true) @RequestParam(value = "gender", required = false) String gender, 
			@Parameter(hidden = true) @RequestParam(value = "status", required = false) Integer status){
		SaveUserDTO user = new SaveUserDTO(name,file,emails,gender,status);
		return new ResponseEntity<GetUserDTO>(userService.updateUser(user, userId), HttpStatus.OK);
	}
	
	@Operation(summary = "Delete user.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "User deleted successfully.", content = {
			@Content(mediaType = "application/json", schema = @Schema(type = "string")) }),
			@ApiResponse(responseCode = "404", description = "User not found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }), })
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
		userService.deleteUser(userId);
		return new ResponseEntity<String>("User deleted User deleted successfully", HttpStatus.OK);
	}
	
	@Operation(summary = "Get photo user.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "photo obtained user.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)) }) })
	@GetMapping("/{id}/photo")
	public ResponseEntity<ImageResponseDTO> getPhotoByUserId(@PathVariable("id") Long userId){
		return new ResponseEntity<ImageResponseDTO>(userService.getPhoto(userId), HttpStatus.OK);
	}
	

}
