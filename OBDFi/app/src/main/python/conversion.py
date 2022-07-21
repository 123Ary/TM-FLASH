def main(hex,bin):
    file = open(hex, "r")
    #file.seek(0x8FD8000)
    ret = -1
    cur_ext_addr = 0
    cur_addr = 0
    nxt_addr = 0
    nxt_ext_addr = 0

    #find the starting extended address
    while(ret == -1):
        data = file.readline()
        ret = data.find("0200000408FD")
        if(ret != -1):
            cur_ext_addr = 0x8FD
        if(data == ""):
            break
    #find the starting address
    ret = -1
    while(ret == -1):
        data = file.readline()
        ret = data.find(":208000")
        if(ret != -1):
            cur_addr = 0x8000
        #if(len(data) < 32):
            #break
    new_data = data[9:-3]
    barr = bytearray.fromhex(new_data)
    #creeate bin file from the data
    bin_file = open(bin, "wb")
    bin_file.write(barr)
    #Convert data till the required size is met
    ret = -1
    while(ret == -1):
        data = file.readline()
        #final address is 0x0957FFFF
        ret = data.find(":020000040958")
        #skip lines containing extended address
        #and convert the rest if the lines
        if(len(data) > 32): #regular address
            nxt_addr = int(data[3:7],16)
            if((nxt_addr - cur_addr) <= 0x20):
                new_data = data[9:-3]
                barr = bytearray.fromhex(new_data)
                #creeate bin file from the data
                bin_file.write(barr)
                cur_addr = nxt_addr
            else:
                #fill the gap within extended address with 0xFF
                print("found gap within extended address segment. filling with 0xFF")
                print(data)
                print(cur_addr," ",nxt_addr)
                diff = nxt_addr - cur_addr
                while(diff != 0):
                    bin_file.write(b'\xFF')
                    diff -= 1
                cur_addr = nxt_addr
        else: #extended address
            print(data[:-3])
            
            #handling the end gap within an extended address section        
            if(cur_addr != 0xFFE0): #This should be the end address within an extended address
                print("found end gap within extended address segment. filling with 0xFF")
                gap = 0x10000-(cur_addr+0x20)
                print(cur_addr)
                print(gap)
                while(gap != 0):
                    bin_file.write(b'\xFF')
                    gap -= 1
            #handling skipping between extended addresses       
            nxt_ext_addr = int(data[10:13],16)        
            d = nxt_ext_addr - cur_ext_addr
            print(nxt_ext_addr," ",cur_ext_addr," ",d)
            if((nxt_ext_addr - cur_ext_addr) > 1):
                print("found gap between extended address segment. filling with 0xFF")
                ext_dif = (nxt_ext_addr - (cur_ext_addr+1))* 0x10000
                while(ext_dif != 0):
                    bin_file.write(b'\xFF')
                    ext_dif -= 1
            cur_ext_addr = nxt_ext_addr
            #reset cur_address
            cur_addr = 0x0000
    file.close()    
    #f = open("flash.bin", "rb")
    #f.seek(0x27FF0)
    #print(f.read(0xF))
    bin_file.close()
    return "success"
