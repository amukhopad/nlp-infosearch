

def bit_not(val: int, max_bits: int):
    return full_bits_int(max_bits) ^ val


def full_bits_int(max_bits: int) -> int:
    return int('1' * max_bits, 2)
