# frozen_string_literal: true
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Message.proto

require 'google/protobuf'


descriptor_data = "\n\rMessage.proto\"?\n\x07Message\x12\x17\n\x06\x64\x65mand\x18\x01 \x01(\x0e\x32\x07.Demand\x12\x1b\n\x08response\x18\x02 \x01(\x0e\x32\t.Response*\x13\n\x06\x44\x65mand\x12\t\n\x05\x43PCTY\x10\x00*&\n\x08Response\x12\x07\n\x03YEP\x10\x00\x12\x07\n\x03NOP\x10\x01\x12\x08\n\x04NULL\x10\x02\x62\x06proto3"

pool = Google::Protobuf::DescriptorPool.generated_pool
pool.add_serialized_file(descriptor_data)

Message = ::Google::Protobuf::DescriptorPool.generated_pool.lookup("Message").msgclass
Demand = ::Google::Protobuf::DescriptorPool.generated_pool.lookup("Demand").enummodule
Response = ::Google::Protobuf::DescriptorPool.generated_pool.lookup("Response").enummodule