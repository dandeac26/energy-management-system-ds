package ro.tuc.ds2020.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.UsersIdsDetailsDTO;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.UsersIdsService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
//@CrossOrigin(origins = "http://localhost:3003")
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;
    private final UsersIdsService usersIdsService;
//    @Autowired
//    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceController(DeviceService deviceService, UsersIdsService usersIdsService) {
        this.deviceService = deviceService;
        this.usersIdsService = usersIdsService;
    }

//    @GetMapping()
//    public ResponseEntity<List<DeviceDTO>> getDevices() {
//        List<DeviceDTO> dtos = deviceService.findDevices();
//        for (DeviceDTO dto : dtos) {
//            Link deviceLink = linkTo(methodOn(DeviceController.class)
//                    .getDevice(dto.getId())).withRel("deviceDetails");
//            dto.add(deviceLink);
//        }
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }


    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        List<DeviceDTO> dtos;

        if (isAdmin) {
            dtos = deviceService.findDevices();
        } else {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            dtos = deviceService.findDevicesByUsername(username);
//            dtos = deviceService.findDevices();
        }

        for (DeviceDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class).getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    @DeleteMapping(value = "/deleteUserId/{userId}")
    public ResponseEntity<Void> deleteUserId(@PathVariable("userId") UUID userId) {
        usersIdsService.deleteUserId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping(value = "/insertUserId")
    public ResponseEntity<UUID> insertUserId(@Valid @RequestBody UsersIdsDetailsDTO usersIdsDTO) {
        UUID UsersIds = usersIdsService.insertUserId(usersIdsDTO);
        return new ResponseEntity<>(UsersIds, HttpStatus.CREATED);
    }
    @PostMapping()
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        UUID deviceID = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDevice(@PathVariable("id") UUID deviceId) {
        DeviceDetailsDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UUID> updateDevice(@PathVariable("id") UUID deviceId, @Valid @RequestBody DeviceDetailsDTO updatedDeviceDTO) {
        UUID updatedDeviceId = deviceService.update(deviceId, updatedDeviceDTO);
        return new ResponseEntity<>(updatedDeviceId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") UUID deviceId) {
        deviceService.delete(deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping(value = "/userDevices/{userId}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByUserId(@PathVariable("userId") UUID userId) {
//        DeviceDetailsDTO dto = deviceService.findDevicesByUserId(userId);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
        List<DeviceDTO> dtos = deviceService.findDevicesByUserId(userId);
        for (DeviceDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
//    @GetMapping("/user/{userId}/devices")
//    public ResponseEntity<List<DeviceDTO>> getDevicesForUser(@PathVariable String userId) {
//        List<Device> devices = deviceRepository.findDevicesByUserId(userId);
//
//        if (devices.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        // You may need to convert the Device objects to DeviceDTO if they have different structures.
//        List<DeviceDTO> deviceDTOs = convertToDTOs(devices);
//
//        return new ResponseEntity<>(deviceDTOs, HttpStatus.OK);
//    }

    @GetMapping("/secured")
    public String securedEndpoint(){return "Secured endpoint accessed";}

    @GetMapping("/admin")
    public String accessAdmin(){return "Accessed admin";}

    @GetMapping("/client")
    public String accessUser(){
        return "accessed client";
    }

    @PostMapping("/client")
    public String accessPOSTUser(){
        return "accessed POST client";
    }
    @DeleteMapping("/client")
    public String accessDelUser(){
        return "accessed DEL client";
    }
    @PutMapping("/client/put")
    public String accessPUTUser(){
        return "accessed PUt client";
    }
}