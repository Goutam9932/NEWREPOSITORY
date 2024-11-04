package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Page<User> findAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	public Optional<User> findByUsername(String username) {
	    return userRepository.findByUsername(username);
	}
	public void saveUser(User user, MultipartFile imageFile) throws IOException {
		if (!imageFile.isEmpty()) {
			user.setImage(imageFile.getBytes());
		}

		// Check for existing username
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new IllegalArgumentException("Username already exists");
		}

		userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public List<User> getAdminDetails() {
		return userRepository.findByRole("ADMIN");
	}

	public List<User> searchUsersByName(String name) {
		return userRepository.findByNameContainingIgnoreCase(name);
	}

	public User findUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
	}

	public void updateAdmin(User user, MultipartFile imageFile) throws IOException {
		User existingUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Update the properties correctly
		existingUser.setUsername(user.getUsername()); // Update the username here
		existingUser.setName(user.getName());
		existingUser.setEmail(user.getEmail());
		existingUser.setRole(user.getRole());

		// Only update the password if it's provided
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the password
		}

		// Handle the image file as needed
		if (!imageFile.isEmpty()) {
			existingUser.setImage(imageFile.getBytes());
		}

		userRepository.save(existingUser); // Save the updated user
	}

	public void updateUser(User updatedUser) {
		User existingUser = userRepository.findById(updatedUser.getId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Update editable fields
		existingUser.setName(updatedUser.getName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setUsername(updatedUser.getUsername());
		existingUser.setPassword(updatedUser.getPassword());

		// Only update the image if a new one is provided
		if (updatedUser.getImage() != null && updatedUser.getImage().length > 0) {
			existingUser.setImage(updatedUser.getImage());
		}

		// Save the updated user to the repository
		userRepository.save(existingUser);
	}

	public void deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("User not found with ID: " + id);
		}
	}
}
