package com.example.thuephong.controller;


import com.example.thuephong.model.House;
import com.example.thuephong.service.IHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/houses")
public class HouseController {
    @Autowired
    IHouseService houseService;

    @GetMapping
    public ResponseEntity<Page<House>> findAllHouse(@PageableDefault(value = 3) Pageable pageable) {
        Page<House> houses = houseService.findAll(pageable);
        if (houses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(houses, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<House> findProductId(@PathVariable Long id) {
        Optional<House> houseOptional = houseService.findById(id);
        if (!houseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(houseOptional.get(), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<House> saveHouse (@RequestParam("file")MultipartFile file, @Valid House house) {
        String fileName = file.getOriginalFilename();
        house.setImage(fileName);
        try {
            file.transferTo(new File("D:\\CodeGym\\Modul4\\File1\\MiniTest\\MIniTest_SpringJPA_Phong_NA\\HienThi\\image\\" + fileName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<>(houseService.save(house), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<House> updateHouse (@RequestParam("file") MultipartFile file, @PathVariable Long id, House house) {
        String fileName = file.getOriginalFilename();
        if (fileName.equals("")){
          house.setImage(houseService.findById(id).get().getImage());
            try {
                file.transferTo(new File("D:\\CodeGym\\Modul4\\File1\\MiniTest\\MIniTest_SpringJPA_Phong_NA\\HienThi\\image\\" + house.getImage()));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        else {
            house.setImage(fileName);
            try {
                file.transferTo(new File("D:\\CodeGym\\Modul4\\File1\\MiniTest\\MIniTest_SpringJPA_Phong_NA\\HienThi\\image\\" + fileName));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        house.setId(id);
        return new ResponseEntity<>(houseService.save(house), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<House> deleteHouse(@PathVariable Long id) {
        Optional<House> houseOptional = houseService.findById(id);
        if (!houseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        houseService.remove(id);
        return new ResponseEntity<>(houseOptional.get(), HttpStatus.NO_CONTENT);
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<House> edit(@RequestParam("file") MultipartFile file, House house,@PathVariable long id){
//        String fileName = file.getOriginalFilename();
//        house.setImage(fileName);
//        try{
//            file.transferTo(new File("D:\\MD4\\demo\\src\\main\\resources\\templates\\post\\img\\"+fileName));
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        house.setId(id);
//        houseService.save(house);
//        return new ResponseEntity<>(house, HttpStatus.OK);
//    }




//    Cái này là hàm save thường
//    @PostMapping
//    public ResponseEntity<Product> saveProduct (@Valid @RequestBody Product product) {
//        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
//    }



//    Cái này không save
//    @PostMapping("/upload")
//    public ResponseEntity<House> handleFileUpload (@RequestParam("file")MultipartFile file, House house) {
//        String fileName = file.getOriginalFilename();
//        house.setImage(fileName);
//        try {
//            file.transferTo(new File("D:\\CodeGym\\Modul4\\File1\\MiniTest\\MIniTest_SpringJPA_Phong_NA\\HienThi\\image\\" + fileName));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(house);
//    }









    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }








}
